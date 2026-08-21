package com.PriceHunter.AuthService.api;

import com.PriceHunter.AuthService.models.dto.RegisterDTO;
import com.PriceHunter.AuthService.models.dto.TokensDto;
import com.PriceHunter.AuthService.models.dto.UpdateAuthDTO;
import com.PriceHunter.AuthService.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokensDto> register(@RequestBody RegisterDTO registerDTO) {
        TokensDto tokens = authService.register(registerDTO);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokensDto> refresh(@RequestBody String refreshToken) {
        TokensDto tokens = authService.rotateToken(refreshToken);
        return ResponseEntity.ok(tokens);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateAuth(@RequestBody UpdateAuthDTO updateAuthDTO) {
        authService.updateAuthModel(updateAuthDTO.getEmail(), updateAuthDTO.getUpdateType());
        return ResponseEntity.ok(String.format("User [%s] updated", updateAuthDTO.getEmail()));
    }
}
