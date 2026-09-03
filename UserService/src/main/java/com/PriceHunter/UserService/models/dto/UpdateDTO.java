package com.PriceHunter.UserService.models.dto;

import com.PriceHunter.UserService.models.domain.NotificationSettingsDomain;
import com.PriceHunter.UserService.models.enums.FieldToUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdateDTO {
    private UUID userId;
    private FieldToUpdate fieldToUpdate;

    private String email;

    private String firstName;
    private String lastName;

    private NotificationSettingsDomain notificationSettings;
}
