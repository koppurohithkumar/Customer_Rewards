package com.example.customer_rewards.controller;

import com.example.customer_rewards.dto.CustomerRewardsResponse;
import com.example.customer_rewards.dto.MonthlyReward;
import com.example.customer_rewards.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    @DisplayName("GET /{id} with fromDate/toDate returns 200 and body")
    void customerWithBounds() throws Exception {
        int id = 1;
        var from = LocalDate.parse("2025-01-01");
        var to = LocalDate.parse("2025-02-28");
        var body = new CustomerRewardsResponse(
                id,
                "Alice",
                List.of(new MonthlyReward("2025-01", 90), new MonthlyReward("2025-02", 25)),
                115
        );

        when(transactionService.rewardsForCustomer(eq(id), eq(Optional.of(from)), eq(Optional.of(to))))
                .thenReturn(body);

        mvc.perform(get("/{customerId}", id)
                        .param("fromDate", "2025-01-01")
                        .param("toDate", "2025-02-28")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.customerName", is("Alice")))
                .andExpect(jsonPath("$.monthlyRewards", hasSize(2)))
                .andExpect(jsonPath("$.totalPoints", is(115)));
    }

    @Test
    @DisplayName("GET /{id} without dates returns 200 and body")
    void customerNoBounds() throws Exception {
        int id = 2;
        var body = new CustomerRewardsResponse(
                id,
                "Bob",
                List.of(new MonthlyReward("2025-03", 40)),
                40
        );

        when(transactionService.rewardsForCustomer(eq(id), eq(Optional.empty()), eq(Optional.empty())))
                .thenReturn(body);

        mvc.perform(get("/{customerId}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId", is(2)))
                .andExpect(jsonPath("$.customerName", is("Bob")))
                .andExpect(jsonPath("$.monthlyRewards", hasSize(1)))
                .andExpect(jsonPath("$.totalPoints", is(40)));
    }

    @Test
    @DisplayName("GET / with fromDate/toDate returns 200 and list")
    void allWithBounds() throws Exception {
        var from = LocalDate.parse("2025-01-01");
        var to = LocalDate.parse("2025-03-31");

        var a = new CustomerRewardsResponse(1, "Alice", List.of(new MonthlyReward("2025-01", 90)), 90);
        var b = new CustomerRewardsResponse(2, "Bob", List.of(new MonthlyReward("2025-03", 40)), 40);

        when(transactionService.rewardsForAll(eq(Optional.of(from)), eq(Optional.of(to))))
                .thenReturn(List.of(a, b));

        mvc.perform(get("/")
                        .param("fromDate", "2025-01-01")
                        .param("toDate", "2025-03-31")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].customerName", is("Alice")))
                .andExpect(jsonPath("$[1].customerName", is("Bob")));
    }

    @Test
    @DisplayName("GET / without dates returns 200 and list")
    void allNoBounds() throws Exception {
        var a = new CustomerRewardsResponse(1, "Alice", List.of(new MonthlyReward("2025-01", 90)), 90);

        when(transactionService.rewardsForAll(eq(Optional.empty()), eq(Optional.empty())))
                .thenReturn(List.of(a));

        mvc.perform(get("/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerId", is(1)));
    }
}