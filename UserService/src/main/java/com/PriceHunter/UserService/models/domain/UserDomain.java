package com.PriceHunter.UserService.models.domain;

import com.PriceHunter.UserService.models.entity.NotificationSettings;
import com.PriceHunter.UserService.models.exceptions.UserArgumentException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class UserDomain {
    private final UUID userId;

    private final String email;

    private final String firstName;
    private final String lastName;

    private final Long chatId;
    private final Long telegramLinkCode;
    private LocalDateTime linkedAt;

    private final NotificationSettings notificationSettings;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private UserDomain(UUID userId, String email, String firstName, String lastName, Long chatId, Long telegramLinkCode, LocalDateTime linkedAt, NotificationSettings notificationSettings, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    public static UserDomain createUserDomain(UUID userId, String email, String firstName, String lastName, Long chatId, Long telegramLinkCode, LocalDateTime linkedAt, NotificationSettings notificationSettings, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (userId == null) {
            throw new UserArgumentException("User id cant be null");
        }

        if (email == null || email.isBlank()) {
            throw new UserArgumentException("Email cant be blank or null");
        }

        if (firstName == null || firstName.isBlank()) {
            throw new UserArgumentException("First name cant be blank or null");
        }

        if (lastName == null || lastName.isBlank()) {
            throw new UserArgumentException("Last name cant be blank or null");
        }

        if (notificationSettings == null) {
            throw new UserArgumentException("Settings cant be null");
        }

        if (createdAt == null) {
            throw new UserArgumentException("Created at cant be null");
        }

        if (updatedAt == null || updatedAt.isBefore(LocalDateTime.now())) {
            throw new UserArgumentException("Updated at cant be null or before than present");
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

    public void updateCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void updateUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
