package com.example.customer_rewards.repository;

import com.example.customer_rewards.model.PurchaseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<PurchaseTransaction, Integer> {


    @Query("SELECT t FROM PurchaseTransaction t WHERE t.customer.customerId = :customerId AND t.localDateTime BETWEEN :from AND :to")
    List<PurchaseTransaction> findByCustomerIdAndRange(@Param("customerId") int customerId,
                                                       @Param("from") LocalDateTime from,
                                                       @Param("to") LocalDateTime to);


    @Query("SELECT t FROM PurchaseTransaction t WHERE t.localDateTime BETWEEN :from AND :to")
    List<PurchaseTransaction> findAllInRange(@Param("from")LocalDateTime from, @Param("to") LocalDateTime to);
}
