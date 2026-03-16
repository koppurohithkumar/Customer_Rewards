package com.example.customer_rewards.controller;

import com.example.customer_rewards.dto.CustomerRewardsResponse;
import com.example.customer_rewards.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CustomerRewardsResponse>> all(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        var body = transactionService.rewardsForAll(
                Optional.ofNullable(fromDate),
                Optional.ofNullable(toDate)
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping(value = "/{customerId}", produces = "application/json")
    public ResponseEntity<CustomerRewardsResponse> byCustomer(
            @PathVariable int customerId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        var body = transactionService.rewardsForCustomer(
                customerId,
                Optional.ofNullable(fromDate),
                Optional.ofNullable(toDate)
        );
        return ResponseEntity.ok(body);
    }

}
