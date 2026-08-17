package com.PriceHunter.AuthService.services;

import com.PriceHunter.AuthService.models.AuthEntity;
import com.PriceHunter.AuthService.models.RefreshToken;
import com.PriceHunter.AuthService.models.Role;
import com.PriceHunter.AuthService.models.domain.AuthDomain;
import com.PriceHunter.AuthService.models.domain.RefreshTokenDomain;
import com.PriceHunter.AuthService.models.dto.RegisterDTO;
import com.PriceHunter.AuthService.models.dto.TokensDto;
import com.PriceHunter.AuthService.models.exceptions.AuthExistsException;
import com.PriceHunter.AuthService.models.exceptions.RefreshTokenNotFoundException;
import com.PriceHunter.AuthService.services.interfaces.AuthMapper;
import com.PriceHunter.AuthService.services.interfaces.AuthRepository;
import com.PriceHunter.AuthService.services.interfaces.RefreshTokenMapper;
import com.PriceHunter.AuthService.services.interfaces.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthService {
    @Value("${access_token_lifetime}")
    private Duration accessTokenLifetime;

    private final SecretKey secretKey;
    private final BCryptPasswordEncoder passwordEncoder;

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenMapper refreshTokenMapper;

    public AuthService(@Value("${secret_key}") String signingKey, BCryptPasswordEncoder passwordEncoder, AuthRepository authRepository, AuthMapper authMapper, RefreshTokenMapper refreshTokenMapper, RefreshTokenRepository refreshTokenRepository) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(signingKey));
        this.passwordEncoder = passwordEncoder;
        this.authRepository = authRepository;
        this.authMapper = authMapper;
        this.refreshTokenMapper = refreshTokenMapper;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateAccessToken(String email, UUID userId) {
        Date createdAt = new Date();
        Date expiresAt = new Date(createdAt.getTime() + accessTokenLifetime.toMillis());

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(createdAt)
                .expiration(expiresAt)
                .signWith(secretKey)
                .compact();
    }

    public TokensDto register(RegisterDTO registerDTO) {
        try {
            String email = registerDTO.getEmail();
            String password = registerDTO.getPassword();

            boolean isAuthExists = authRepository.findAuthEntityByEmail(email).isPresent();
            if (isAuthExists) {
                throw new AuthExistsException(String.format("Auth with email - %s exists", email));
            }

            String encodedPassword = passwordEncoder.encode(password);
            AuthEntity createdAuth = authMapper.domainToEntity(AuthDomain.createAuthModel(email, encodedPassword, Role.USER, true, false, LocalDateTime.now(), LocalDateTime.now()));
            authRepository.save(createdAuth);
            log.info("Saved new auth, id - {}", createdAuth.getId());

            String refreshTokenText = UUID.randomUUID().toString() + UUID.randomUUID();
            String refreshTokenHash = passwordEncoder.encode(refreshTokenText);
            RefreshToken refreshToken = refreshTokenMapper.domainToEntity(RefreshTokenDomain.createRefreshToken(createdAuth.getId(), createdAuth.getEmail(), refreshTokenHash, LocalDateTime.now(), LocalDateTime.now().plusDays(20)));
            refreshTokenRepository.save(refreshToken);

            String accessToken = generateAccessToken(email, createdAuth.getId());

            return TokensDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshTokenText)
                    .build();
        } catch (Exception e) {
            log.error("Unexpected error while registering user: {}", e.getMessage());
            throw new RuntimeException(String.format("Internal server error: %s", e.getMessage()));
        }

    }

    @Transactional
    public TokensDto rotateToken(String refreshTokenText) {
        try {
            //TODO поиск токена рефреш токена

            if (refreshToken.isEmpty()) {
                throw new RefreshTokenNotFoundException(String.format("Refresh token %s not found", refreshToken));
            }

            refreshToken.get().setRevoked(true);
            String newRefreshTokenText = UUID.randomUUID().toString() + UUID.randomUUID();
            saveNewRefreshToken(newRefreshTokenText, refreshToken.get().getUserId(), refreshToken.get().getEmail());

            String accessToken = generateAccessToken(refreshToken.get().getEmail(), refreshToken.get().getUserId());

            return TokensDto.builder()
                    .refreshToken(newRefreshTokenText)
                    .accessToken(accessToken)
                    .build();
        } catch (Exception e) {
            log.error("Error while rotating token: {}", e.getMessage());
            throw new RuntimeException(String.format("Internal server error: %s", e.getMessage()));
        }
    }

    private void saveNewRefreshToken(String refreshTokenText, UUID authId, String email) {
        try {
            String refreshTokenHash = passwordEncoder.encode(refreshTokenText);
            RefreshToken refreshToken = refreshTokenMapper.domainToEntity(RefreshTokenDomain.createRefreshToken(authId, email, refreshTokenHash, LocalDateTime.now(), LocalDateTime.now().plusDays(20)));
            refreshTokenRepository.save(refreshToken);
        } catch (Exception e) {
            log.error("Error saving refresh token: {}", e.getMessage());
            throw new RuntimeException(String.format("Internal server error: %s", e.getMessage()));
        }
    }
}
