package com.PriceHunter.UserService.api;

import com.PriceHunter.UserService.models.domain.UserDomain;
import com.PriceHunter.UserService.models.dto.NotificationSettingsDTO;
import com.PriceHunter.UserService.models.dto.UpdateDTO;
import com.PriceHunter.UserService.models.dto.UserDTO;
import com.PriceHunter.UserService.service.UserService;
import com.PriceHunter.UserService.service.interfaces.NotificationSettingsMapper;
import com.PriceHunter.UserService.service.interfaces.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final NotificationSettingsMapper notificationSettingsMapper;

    @GetMapping()
    public ResponseEntity<UserDTO> getSelfInfo(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        UserDomain domainUser = userService.findUserByUserId(userId);
        NotificationSettingsDTO settings = notificationSettingsMapper.domainToDto(domainUser.getNotificationSettings());
        UserDTO user = userMapper.domainToDto(domainUser, settings);

        return ResponseEntity.ok(user);
    }

    @PatchMapping()
    public ResponseEntity<String> updateProfile(@RequestBody UpdateDTO updateDTO, HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        userService.updateUser(updateDTO, userId);

        return ResponseEntity.ok("Your profile successfully updated");
    }
}
