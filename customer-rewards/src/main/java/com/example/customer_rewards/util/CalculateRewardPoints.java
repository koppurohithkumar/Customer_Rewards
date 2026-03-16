package com.example.customer_rewards.util;

public class CalculateRewardPoints {
    public static int calculateRewardPoints(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount cannot be negative");
        }

        if (amount > 100) {
            return 50 + (amount - 100) * 2;
        } else if (amount > 50) {
            return amount - 50;
        } else {
            return 0;
        }
    }

    }
