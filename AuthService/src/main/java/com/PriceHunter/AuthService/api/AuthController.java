package com.PriceHunter.AuthService.api;

import com.PriceHunter.AuthService.models.dto.LoginDTO;
import com.PriceHunter.AuthService.models.dto.RegisterDTO;
import com.PriceHunter.AuthService.models.dto.TokensDTO;
import com.PriceHunter.AuthService.models.dto.UpdateAuthDTO;
import com.PriceHunter.AuthService.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokensDTO> register(@RequestBody @Valid RegisterDTO registerDTO) {
        TokensDTO tokens = authService.register(registerDTO);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokensDTO> refresh(@RequestBody String refreshToken) {
        TokensDTO tokens = authService.rotateToken(refreshToken);
        return ResponseEntity.ok(tokens);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateAuth(@RequestBody @Valid UpdateAuthDTO updateAuthDTO) {
        authService.updateAuthModel(updateAuthDTO.getEmail(), updateAuthDTO.getUpdateType());
        return ResponseEntity.ok(String.format("User [%s] updated", updateAuthDTO.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<TokensDTO> login(@RequestBody LoginDTO loginDTO) {
        TokensDTO tokens = authService.login(loginDTO);
        return ResponseEntity.ok(tokens);
    }
}
