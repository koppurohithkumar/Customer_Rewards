package com.example.customer_rewards.service;

import com.example.customer_rewards.dto.CustomerRewardsResponse;
import com.example.customer_rewards.dto.MonthlyReward;
import com.example.customer_rewards.exception.BadRequestException;
import com.example.customer_rewards.exception.NotFoundException;
import com.example.customer_rewards.model.Customer;
import com.example.customer_rewards.model.PurchaseTransaction;
import com.example.customer_rewards.repository.CustomerRepo;
import com.example.customer_rewards.repository.TransactionRepo;
import com.example.customer_rewards.util.CalculateRewardPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private CustomerRepo customerRepo;

    public CustomerRewardsResponse rewardsForCustomer(int customerId, Optional<LocalDate> from, Optional<LocalDate> to){
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new NotFoundException("user not found"));
        if(transactionRepo.count() == 0) throw new NotFoundException("No Data");
        DateRange dataRange = resolveRange(from, to);
        List<PurchaseTransaction> purchaseTransactions =  transactionRepo.findByCustomerIdAndRange(customerId, dataRange.from, dataRange.to);
        List<MonthlyReward> mr = purchaseTransactions.stream().collect(Collectors.groupingBy(t -> YearMonth.from(t.getLocalDateTime().toLocalDate()), TreeMap::new , Collectors.summingInt(t -> CalculateRewardPoints.calculateRewardPoints(t.getAmount()))) )
                .entrySet().stream().map(e -> new MonthlyReward(e.getKey().toString(),e.getValue())).toList();
        int tp = mr.stream().mapToInt(e -> e.getPoints()).sum();
        return new CustomerRewardsResponse(customer.getCustomerId(), customer.getName(), mr, tp);
    }

    public List<CustomerRewardsResponse> rewardsForAll(Optional<LocalDate> from, Optional<LocalDate> to)  {
        if(transactionRepo.count() == 0) throw new NotFoundException("No Data");
        DateRange dataRange = resolveRange(from, to);
        List<PurchaseTransaction> purchaseTransactions =  transactionRepo.findAllInRange(dataRange.from,dataRange.to);

        Map<Integer, String> idToName = purchaseTransactions.stream()
                .map(t -> t.getCustomer())
                .collect(Collectors.toMap(Customer::getCustomerId, Customer::getName, (a,b) -> a));

        Map<Integer, Map<YearMonth, Integer>> byCustomerThenMonth = purchaseTransactions.stream()
                .collect(Collectors.groupingBy(t -> t.getCustomer().getCustomerId(),
                        Collectors.groupingBy(
                                t -> YearMonth.from(t.getLocalDateTime().toLocalDate()),
                                TreeMap :: new,
                                Collectors.summingInt(
                                        t -> CalculateRewardPoints.calculateRewardPoints(t.getAmount())
                                )
                        )));

        List<CustomerRewardsResponse> response = byCustomerThenMonth.entrySet().stream().
                map(e -> {

                    int customerId = e.getKey();

                    String customerName = idToName.getOrDefault(customerId, "customerId : " + customerId);

                    List<MonthlyReward> monthlyRewards = e.getValue().entrySet().stream().map(mr ->
                            new MonthlyReward(mr.getKey().toString(), mr.getValue())).toList();

                    int tp = e.getValue().values().stream().mapToInt(Integer::intValue).sum();

                    return new CustomerRewardsResponse(customerId, customerName, monthlyRewards, tp);
                }
                ).sorted(Comparator.comparing(CustomerRewardsResponse::getCustomerName)).toList();

        return response;
    }

    public DateRange resolveRange(Optional<LocalDate> from, Optional<LocalDate> to)  {
        if (from.isPresent() && to.isPresent() && from.get().isAfter(to.get())) {
            throw new BadRequestException("from must be before or equal to to");
        }
        LocalDate start;
        LocalDate end;
        if (from.isPresent() || to.isPresent()) {
            start = from.orElseGet(() -> to.orElseThrow());
            end   = to.orElseGet(() -> from.orElseThrow());
        } else {
            List<PurchaseTransaction> allPurchaseTransactions = transactionRepo.findAll();
            start = allPurchaseTransactions.stream()
                    .map(t -> t.getLocalDateTime().toLocalDate())
                    .min(LocalDate::compareTo)
                    .orElseThrow();
            end = allPurchaseTransactions.stream()
                    .map(t -> t.getLocalDateTime().toLocalDate())
                    .max(LocalDate::compareTo)
                    .orElseThrow();
        }
        return new DateRange(start.atStartOfDay(), end.atTime(23,59,59,999_000_000));
    }

    record DateRange(LocalDateTime from, LocalDateTime to){ }
}
