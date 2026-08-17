package com.PriceHunter.AuthService.services;

import com.PriceHunter.AuthService.models.AuthEntity;
import com.PriceHunter.AuthService.models.RefreshToken;
import com.PriceHunter.AuthService.models.Role;
import com.PriceHunter.AuthService.models.domain.AuthDomain;
import com.PriceHunter.AuthService.models.domain.RefreshTokenDomain;
import com.PriceHunter.AuthService.models.dto.RegisterDTO;
import com.PriceHunter.AuthService.models.dto.TokensDto;
import com.PriceHunter.AuthService.models.exceptions.AuthExistsException;
import com.PriceHunter.AuthService.services.interfaces.AuthMapper;
import com.PriceHunter.AuthService.services.interfaces.AuthRepository;
import com.PriceHunter.AuthService.services.interfaces.RefreshTokenMapper;
import com.PriceHunter.AuthService.services.interfaces.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
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
        String email = registerDTO.getEmail();
        String password = registerDTO.getPassword();

        boolean isAuthExists = authRepository.findAuthEntityByEmail(email).isPresent();
        if (isAuthExists) {
            throw new AuthExistsException(String.format("Auth with email - %s exists", email));
        }

        String encodedPassword = passwordEncoder.encode(password);
        AuthEntity createdAuth = authMapper.domainToEntity(AuthDomain.createAuthModel(email, encodedPassword, Role.USER, true, false, LocalDateTime.now(), LocalDateTime.now()));
        authRepository.save(createdAuth);

        String refreshTokenText = UUID.randomUUID().toString() + UUID.randomUUID();
        String refreshTokenHash = passwordEncoder.encode(refreshTokenText);
        RefreshToken refreshToken = refreshTokenMapper.domainToEntity(RefreshTokenDomain.createRefreshToken(createdAuth.getId(), refreshTokenHash, LocalDateTime.now(), LocalDateTime.now().plusDays(20), false));
        refreshTokenRepository.save(refreshToken);

        String accessToken = generateAccessToken(email, createdAuth.getId());

        return TokensDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenText)
                .build();
    }
}
