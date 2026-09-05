package com.PriceHunter.UserService.models.domain;

import com.PriceHunter.UserService.models.exceptions.UserArgumentException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class UserDomain {
    private final UUID userId;

    private String email;

    private String firstName;
    private String lastName;

    private final Long chatId;
    private final Long telegramLinkCode;
    private LocalDateTime linkedAt;

    private NotificationSettingsDomain notificationSettings;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private UserDomain(UUID userId, String email, String firstName, String lastName, Long chatId, Long telegramLinkCode, LocalDateTime linkedAt, NotificationSettingsDomain notificationSettings, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.chatId = chatId;
        this.telegramLinkCode = telegramLinkCode;
        this.linkedAt = linkedAt;
        this.notificationSettings = notificationSettings;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserDomain createUserDomain(UUID userId, String email, String firstName, String lastName, Long chatId, Long telegramLinkCode, LocalDateTime linkedAt, NotificationSettingsDomain notificationSettings, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (userId == null) {
            throw new UserArgumentException("User id cant be null");
        }

        if (email == null || email.isBlank()) {
            throw new UserArgumentException("Email cant be blank or null");
        }

        if (notificationSettings == null) {
            throw new UserArgumentException("Settings cant be null");
        }

        if (createdAt == null) {
            throw new UserArgumentException("Created at cant be null");
        }

        if (updatedAt == null) {
            throw new UserArgumentException("Updated at cant be null");
        }

        return new UserDomain(userId, email, firstName, lastName, chatId, telegramLinkCode, linkedAt, notificationSettings, createdAt, updatedAt);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserDomain that)) return false;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

    public void updateLinkedAt(LocalDateTime linkedAt) {
        this.linkedAt = linkedAt;
    }

    public void updateUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void updateEmail(String email) {
        if (email == null || this.email.equals(email))  {
            throw new UserArgumentException(String.format("New field [%s] cant be null or same", email));
        }
        this.email = email;
    }

    public void updateFirstname(String firstName) {
        if (firstName == null || this.firstName.equals(firstName)) {
            throw new UserArgumentException(String.format("New field [%s] cant be null or same", firstName));
        }
        this.firstName = firstName;
    }

    public void updateLastname(String lastName) {
        if (lastName == null || this.lastName.equals(lastName)) {
            throw new UserArgumentException(String.format("New field [%s] cant be null or same", lastName));
        }
        this.lastName = lastName;
    }

    public void updateNotificationSettings(NotificationSettingsDomain notificationSettings) {
        if (notificationSettings == null || this.notificationSettings.equals(notificationSettings)) {
            throw new UserArgumentException(String.format("New field [%s] cant be null or same", notificationSettings));
        }
        this.notificationSettings = notificationSettings;
    }
}
