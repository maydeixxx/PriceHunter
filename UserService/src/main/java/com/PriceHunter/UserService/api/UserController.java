package com.PriceHunter.UserService.api;

import com.PriceHunter.UserService.models.domain.UserDomain;
import com.PriceHunter.UserService.models.dto.NotificationSettingsDTO;
import com.PriceHunter.UserService.models.dto.UpdateDTO;
import com.PriceHunter.UserService.models.dto.UserDTO;
import com.PriceHunter.UserService.service.UserService;
import com.PriceHunter.UserService.service.interfaces.NotificationSettingsMapper;
import com.PriceHunter.UserService.service.interfaces.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final NotificationSettingsMapper notificationSettingsMapper;

    @GetMapping()
    public ResponseEntity<UserDTO> getSelfInfo() {
        UUID userId = (UUID) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        UserDomain domainUser = userService.findUserByUserId(userId);
        NotificationSettingsDTO settings = notificationSettingsMapper.domainToDto(domainUser.getNotificationSettings());
        UserDTO user = userMapper.domainToDto(domainUser, settings);

        return ResponseEntity.ok(user);
    }

    @PatchMapping()
    public ResponseEntity<String> updateProfile(@RequestBody UpdateDTO updateDTO) {
        UUID userId = (UUID) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        userService.updateUser(updateDTO, userId);

        return ResponseEntity.ok("Your profile successfully updated");
    }
}
