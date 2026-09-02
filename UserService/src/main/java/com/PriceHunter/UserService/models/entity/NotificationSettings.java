package com.PriceHunter.UserService.models.entity;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class NotificationSettings {
    private boolean notifyOnPriceDrop;
    private Integer checkIntervalInMinutes;
    private BigDecimal defaultDropThresholdPercent;
}
