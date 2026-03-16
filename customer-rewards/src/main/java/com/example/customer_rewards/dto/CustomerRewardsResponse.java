package com.example.customer_rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CustomerRewardsResponse {
    private int customerId;
    private String customerName;
    private List<MonthlyReward> monthlyRewards;
    private int totalPoints;
}
