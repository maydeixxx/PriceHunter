package com.PriceHunter.UserService.service;

import com.PriceHunter.UserService.models.domain.NotificationSettingsDomain;
import com.PriceHunter.UserService.models.domain.UserDomain;
import com.PriceHunter.UserService.models.dto.UpdateDTO;
import com.PriceHunter.UserService.models.enums.FieldToUpdate;
import com.PriceHunter.UserService.models.eventModels.AuthCreatedEventModel;
import com.PriceHunter.UserService.models.exceptions.KafkaListenerException;
import com.PriceHunter.UserService.models.exceptions.UserNotFoundException;
import com.PriceHunter.UserService.models.exceptions.UserUpdateException;
import com.PriceHunter.UserService.service.interfaces.UserMapper;
import com.PriceHunter.UserService.service.interfaces.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private void saveUser(UserDomain userDomain) {
        try {
            userRepository.save(userMapper.domainToEntity(userDomain));
        } catch (Exception e) {
            log.error("Error saving user: {}", e.getMessage());
            throw new RuntimeException("Server internal error: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "${kafka.topics.auth-created}", groupId = "userService")
    private void handleAuthCreatedEvent(ConsumerRecord<UUID, AuthCreatedEventModel> record) {
        try {
            UUID userId = record.key();
            AuthCreatedEventModel model = record.value();

            if (userId == null || model == null) {
                throw new KafkaListenerException("User id or auth model is null");
            }

            NotificationSettingsDomain notificationSettings = NotificationSettingsDomain.create(false, 0, new BigDecimal("0"));
            UserDomain userDomain = UserDomain.createUserDomain(userId, model.getEmail(), "", "", null, null, null, notificationSettings, model.getCreatedAt(), model.getUpdatedAt());
            saveUser(userDomain);
        } catch (KafkaListenerException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error handling auth created event");
            throw new RuntimeException("Server internal error: " + e.getMessage());
        }
    }

    @Transactional
    public void updateUser(UpdateDTO updateDTO) {
        try {
            FieldToUpdate fieldToUpdate = updateDTO.getFieldToUpdate();
            if (fieldToUpdate == null) {
                throw new NullPointerException("Field to update is null");
            }

            UserDomain user = userMapper.entityToDomain(userRepository.findUserByUserId(updateDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found")));

            switch (fieldToUpdate) {
                case EMAIL -> user.updateEmail(updateDTO.getEmail());
                case FIRST_NAME -> user.updateFirstname(updateDTO.getFirstName());
                case LAST_NAME -> user.updateLastname(updateDTO.getLastName());
                case NOTIFICATION_SETTINGS -> user.updateNotificationSettings(updateDTO.getNotificationSettings());
                default ->
                        throw new UserUpdateException(String.format("Field to update is unknown [%s]", fieldToUpdate));
            }

            user.updateUpdatedAt(LocalDateTime.now());
            userRepository.save(userMapper.domainToEntity(user));
        } catch (NullPointerException | UserUpdateException e) {
            log.error("Error updating user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unknown error updating user: {}", e.getMessage());
            throw new RuntimeException("Server internal error: " + e.getMessage());
        }
    }
}
