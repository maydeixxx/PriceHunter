package com.PriceHunter.UserService.models.domain;

import com.PriceHunter.UserService.models.exceptions.NotificationSettingsArgException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class NotificationSettingsDomain {
    private final Boolean notifyOnPriceDrop;
    private final Integer checkIntervalInMinutes;
    private final BigDecimal defaultDropThresholdPercent;

    private NotificationSettingsDomain(boolean notifyOnPriceDrop, Integer checkIntervalInMinutes, BigDecimal defaultDropThresholdPercent) {
        this.notifyOnPriceDrop = notifyOnPriceDrop;
        this.checkIntervalInMinutes = checkIntervalInMinutes;
        this.defaultDropThresholdPercent = defaultDropThresholdPercent;
    }

    public static NotificationSettingsDomain create(Boolean notifyOnPriceDrop, Integer checkIntervalInMinutes, BigDecimal defaultDropThresholdPercent) {
        if (notifyOnPriceDrop == null) {
            throw new NotificationSettingsArgException("NotifyOnPriceDrop cant be null");
        }

        if (notifyOnPriceDrop && (checkIntervalInMinutes == null || (defaultDropThresholdPercent == null || defaultDropThresholdPercent.compareTo(new BigDecimal("0")) <= 0))) {
            throw new NotificationSettingsArgException("When notifications enabled other args cant be null");
        }

        return new NotificationSettingsDomain(notifyOnPriceDrop, checkIntervalInMinutes, defaultDropThresholdPercent);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NotificationSettingsDomain that)) return false;
        return Objects.equals(notifyOnPriceDrop, that.notifyOnPriceDrop) && Objects.equals(checkIntervalInMinutes, that.checkIntervalInMinutes) && Objects.equals(defaultDropThresholdPercent, that.defaultDropThresholdPercent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notifyOnPriceDrop, checkIntervalInMinutes, defaultDropThresholdPercent);
    }
}