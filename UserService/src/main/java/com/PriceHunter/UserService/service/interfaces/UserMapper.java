package com.PriceHunter.UserService.service.interfaces;

import com.PriceHunter.UserService.models.domain.NotificationSettingsDomain;
import com.PriceHunter.UserService.models.domain.UserDomain;
import com.PriceHunter.UserService.models.dto.NotificationSettingsDTO;
import com.PriceHunter.UserService.models.dto.UserDTO;
import com.PriceHunter.UserService.models.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User domainToEntity(UserDomain userDomain);

    default UserDomain entityToDomain(User user, NotificationSettingsDomain notificationSettings) {
        return UserDomain.createUserDomain(
                user.getUserId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getChatId(),
                user.getTelegramLinkCode(),
                user.getLinkedAt(),
                notificationSettings,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    default UserDTO domainToDto(UserDomain userDomain, NotificationSettingsDTO settings) {
        return UserDTO.builder()
                .email(userDomain.getEmail())
                .firstName(userDomain.getFirstName())
                .lastName(userDomain.getLastName())
                .notificationSettingsDomain(settings)
                .build();
    }
}
