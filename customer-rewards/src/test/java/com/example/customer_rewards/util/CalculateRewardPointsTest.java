package com.example.customer_rewards.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CalculateRewardPointsTest {

    @Test
    @DisplayName("amount <= 50 yields 0")
    void le50() {
        assertThat(CalculateRewardPoints.calculateRewardPoints(0)).isEqualTo(0);
        assertThat(CalculateRewardPoints.calculateRewardPoints(50)).isEqualTo(0);
    }

    @Test
    @DisplayName("51..100 yields 1 per dollar over 50")
    void between51And100() {
        assertThat(CalculateRewardPoints.calculateRewardPoints(51)).isEqualTo(1);
        assertThat(CalculateRewardPoints.calculateRewardPoints(100)).isEqualTo(50);
        assertThat(CalculateRewardPoints.calculateRewardPoints(75)).isEqualTo(25);
    }

    @Test
    @DisplayName(">100 yields 2 per dollar over 100 plus 50 for 51..100")
    void over100() {
        assertThat(CalculateRewardPoints.calculateRewardPoints(120)).isEqualTo(90);
        assertThat(CalculateRewardPoints.calculateRewardPoints(101)).isEqualTo(52);
        assertThat(CalculateRewardPoints.calculateRewardPoints(200)).isEqualTo(250);
    }
}