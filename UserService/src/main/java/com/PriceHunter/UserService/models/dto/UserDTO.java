package com.PriceHunter.UserService.models.dto;

import lombok.*;


@Builder
@Value
public class UserDTO {
    String email;

    String firstName;
    String lastName;

    NotificationSettingsDTO notificationSettingsDomain;
}
