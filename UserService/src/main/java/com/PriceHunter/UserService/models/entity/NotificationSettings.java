package com.PriceHunter.UserService.models.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class NotificationSettings {
    private Boolean notifyOnPriceDrop;
    private Integer checkIntervalInMinutes;
    private BigDecimal defaultDropThresholdPercent;
}
