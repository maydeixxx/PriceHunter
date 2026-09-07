package com.PriceHunter.UserService.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String email;

    private String firstName;
    private String lastName;

    private Long chatId;
    private Long telegramLinkCode;
    private LocalDateTime linkedAt;

    @Embedded
    private NotificationSettings notificationSettings;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }
}
