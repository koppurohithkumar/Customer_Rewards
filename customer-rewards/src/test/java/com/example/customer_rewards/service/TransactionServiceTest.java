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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepo transactionRepo;

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    private TransactionService service;

    @Test
    @DisplayName("rewardsForCustomer: throws when repo has no data")
    void rewardsForCustomer_noData() {
        int id = 1;
        Customer c = customer(id, "Alice");
        when(customerRepo.findById(id)).thenReturn(Optional.of(c));
        when(transactionRepo.count()).thenReturn(0L);

        assertThrows(NotFoundException.class, () -> service.rewardsForCustomer(id, Optional.empty(), Optional.empty()));
    }

    @Test
    @DisplayName("rewardsForCustomer: with bounds aggregates months and totals")
    void rewardsForCustomer_withBounds() {
        int id = 1;
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to = LocalDate.of(2025, 2, 28);
        LocalDateTime f = from.atStartOfDay();
        LocalDateTime t = to.atTime(23, 59, 59, 999_000_000);

        Customer c = customer(id, "Alice");
        when(customerRepo.findById(id)).thenReturn(Optional.of(c));
        when(transactionRepo.count()).thenReturn(2L);

        var jan = tx(120, LocalDateTime.of(2025, 1, 5, 10, 0), c);
        var feb = tx(75, LocalDateTime.of(2025, 2, 10, 12, 0), c);

        when(transactionRepo.findByCustomerIdAndRange(eq(id), eq(f), eq(t))).thenReturn(List.of(jan, feb));

        CustomerRewardsResponse resp = service.rewardsForCustomer(id, Optional.of(from), Optional.of(to));

        int expected = CalculateRewardPoints.calculateRewardPoints(120)
                + CalculateRewardPoints.calculateRewardPoints(75);

        assertThat(resp.getCustomerId()).isEqualTo(id);
        assertThat(resp.getCustomerName()).isEqualTo("Alice");
        assertThat(resp.getMonthlyRewards()).extracting(MonthlyReward::getMonth).containsExactly("2025-01", "2025-02");
        assertThat(resp.getTotalPoints()).isEqualTo(expected);
    }

    @Test
    @DisplayName("rewardsForAll: no bounds uses min/max then queries range")
    void rewardsForAll_noBounds() {
        when(transactionRepo.count()).thenReturn(3L);

        var alice = customer(1, "Alice");
        var bob = customer(2, "Bob");

        var t1 = tx(120, LocalDateTime.of(2025, 1, 2, 9, 0), alice);
        var t2 = tx(75, LocalDateTime.of(2025, 3, 10, 16, 0), bob);
        var t3 = tx(60, LocalDateTime.of(2025, 1, 15, 13, 0), alice);

        when(transactionRepo.findAll()).thenReturn(List.of(t1, t2, t3));
        when(transactionRepo.findAllInRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(t1, t2, t3));

        var result = service.rewardsForAll(Optional.empty(), Optional.empty());

        ArgumentCaptor<LocalDateTime> fc = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> tc = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(transactionRepo).findAllInRange(fc.capture(), tc.capture());

        assertThat(fc.getValue()).isEqualTo(LocalDate.of(2025, 1, 2).atStartOfDay());
        assertThat(tc.getValue()).isEqualTo(LocalDate.of(2025, 3, 10).atTime(23, 59, 59, 999_000_000));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCustomerName()).isEqualTo("Alice");
        assertThat(result.get(1).getCustomerName()).isEqualTo("Bob");
    }

    @Test
    @DisplayName("resolveRange: from > to throws")
    void resolveRange_invalid() {
        var from = LocalDate.of(2025, 3, 1);
        var to = LocalDate.of(2025, 2, 1);
        assertThrows(BadRequestException.class, () -> service.resolveRange(Optional.of(from), Optional.of(to)));
    }

    @Test
    @DisplayName("resolveRange: only from -> same-day range")
    void resolveRange_onlyFrom() {
        var d = LocalDate.of(2025, 1, 15);
        var r = service.resolveRange(Optional.of(d), Optional.empty());
        assertThat(r.from()).isEqualTo(d.atStartOfDay());
        assertThat(r.to()).isEqualTo(d.atTime(23, 59, 59, 999_000_000));
    }

    @Test
    @DisplayName("resolveRange: only to -> same-day range")
    void resolveRange_onlyTo() {
        var d = LocalDate.of(2025, 2, 20);
        var r = service.resolveRange(Optional.empty(), Optional.of(d));
        assertThat(r.from()).isEqualTo(d.atStartOfDay());
        assertThat(r.to()).isEqualTo(d.atTime(23, 59, 59, 999_000_000));
    }

    @Test
    @DisplayName("resolveRange: no bounds -> min/max from findAll")
    void resolveRange_noBounds() {
        var c = customer(1, "Alice");
        var t1 = tx(10, LocalDateTime.of(2025, 1, 10, 9, 0), c);
        var t2 = tx(20, LocalDateTime.of(2025, 2, 5, 15, 0), c);
        var t3 = tx(30, LocalDateTime.of(2025, 1, 1, 8, 0), c);
        var t4 = tx(40, LocalDateTime.of(2025, 3, 31, 23, 0), c);

        when(transactionRepo.findAll()).thenReturn(List.of(t1, t2, t3, t4));

        var r = service.resolveRange(Optional.empty(), Optional.empty());

        assertThat(r.from()).isEqualTo(LocalDate.of(2025, 1, 1).atStartOfDay());
        assertThat(r.to()).isEqualTo(LocalDate.of(2025, 3, 31).atTime(23, 59, 59, 999_000_000));
    }

    private Customer customer(int id, String name) {
        Customer c = new Customer();
        c.setCustomerId(id);
        c.setName(name);
        return c;
    }

    private PurchaseTransaction tx(int amount, LocalDateTime when, Customer customer) {
        PurchaseTransaction t = new PurchaseTransaction();
        t.setAmount(amount);
        t.setLocalDateTime(when);
        t.setCustomer(customer);
        return t;
    }
}