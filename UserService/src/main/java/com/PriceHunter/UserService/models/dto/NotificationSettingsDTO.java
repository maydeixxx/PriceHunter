package com.PriceHunter.UserService.models.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public class NotificationSettingsDTO {
    Boolean notifyOnPriceDrop;
    Integer checkIntervalInMinutes;
    BigDecimal defaultDropThresholdPercent;
}
