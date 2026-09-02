package com.PriceHunter.UserService.service;

import com.PriceHunter.UserService.models.domain.UserDomain;
import com.PriceHunter.UserService.models.eventModels.AuthCreatedEventModel;
import com.PriceHunter.UserService.models.exceptions.KafkaListenerException;
import com.PriceHunter.UserService.service.interfaces.UserMapper;
import com.PriceHunter.UserService.service.interfaces.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private void saveUser(UserDomain userDomain) {
        userRepository.save(userMapper.domainToEntity(userDomain));
    }

    @KafkaListener(topics = "${kafka.topics.auth-created}", groupId = "userService")
    private void handleAuthCreatedEvent(ConsumerRecord<UUID, AuthCreatedEventModel> record) {
        UUID userId = record.key();
        AuthCreatedEventModel model = record.value();

        if (userId == null || model == null) {
            throw new KafkaListenerException("User id or auth model is null");
        }

        UserDomain userDomain = UserDomain.createUserDomain(userId, model.getEmail(), "", "", null, null, null, null, model.getCreatedAt(), model.getUpdatedAt());
        saveUser(userDomain);
    }
}
