package com.PriceHunter.UserService.service.interfaces;

import com.PriceHunter.UserService.models.domain.NotificationSettingsDomain;
import com.PriceHunter.UserService.models.dto.NotificationSettingsDTO;
import com.PriceHunter.UserService.models.entity.NotificationSettings;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationSettingsMapper {
    NotificationSettings domainToEntity(NotificationSettingsDomain domain);

    default NotificationSettingsDomain entityToDomain(NotificationSettings entity) {
        return NotificationSettingsDomain.create(entity.getNotifyOnPriceDrop(), entity.getCheckIntervalInMinutes(), entity.getDefaultDropThresholdPercent());
    }

    default NotificationSettingsDTO domainToDto(NotificationSettingsDomain notificationSettingsDomain) {
        return NotificationSettingsDTO.builder()
                .checkIntervalInMinutes(notificationSettingsDomain.getCheckIntervalInMinutes())
                .defaultDropThresholdPercent(notificationSettingsDomain.getDefaultDropThresholdPercent())
                .notifyOnPriceDrop(notificationSettingsDomain.getNotifyOnPriceDrop())
                .build();
    }
}
