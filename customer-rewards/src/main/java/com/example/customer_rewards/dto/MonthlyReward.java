package com.example.customer_rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MonthlyReward {
    private String month;
    private int points;
}
