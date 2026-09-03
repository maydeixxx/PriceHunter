package com.PriceHunter.AuthService.services;

import com.PriceHunter.AuthService.eventModels.AuthCreatedEventModel;
import com.PriceHunter.AuthService.models.AuthEntity;
import com.PriceHunter.AuthService.models.RefreshToken;
import com.PriceHunter.AuthService.models.dto.LoginDTO;
import com.PriceHunter.AuthService.models.enums.Role;
import com.PriceHunter.AuthService.models.domain.AuthDomain;
import com.PriceHunter.AuthService.models.domain.RefreshTokenDomain;
import com.PriceHunter.AuthService.models.dto.RegisterDTO;
import com.PriceHunter.AuthService.models.dto.TokensDTO;
import com.PriceHunter.AuthService.models.enums.UpdateType;
import com.PriceHunter.AuthService.models.exceptions.*;
import com.PriceHunter.AuthService.services.interfaces.AuthMapper;
import com.PriceHunter.AuthService.services.interfaces.AuthRepository;
import com.PriceHunter.AuthService.services.interfaces.RefreshTokenMapper;
import com.PriceHunter.AuthService.services.interfaces.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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

    private final KafkaTemplate<UUID, AuthCreatedEventModel> newAuthEventKafkaTemplate;

    public AuthService(@Value("${secret_key}") String signingKey, BCryptPasswordEncoder passwordEncoder,
                       AuthRepository authRepository,
                       AuthMapper authMapper, RefreshTokenMapper refreshTokenMapper,
                       RefreshTokenRepository refreshTokenRepository, @Qualifier("newAuthEventKafkaTemplate") KafkaTemplate<UUID, AuthCreatedEventModel> kafkaTemplate) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(signingKey));
        this.passwordEncoder = passwordEncoder;
        this.authRepository = authRepository;
        this.authMapper = authMapper;
        this.refreshTokenMapper = refreshTokenMapper;
        this.refreshTokenRepository = refreshTokenRepository;
        this.newAuthEventKafkaTemplate = kafkaTemplate;
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

    public TokensDTO register(RegisterDTO registerDTO) {
        try {
            String email = registerDTO.getEmail();
            String password = registerDTO.getPassword();

            boolean isAuthExists = authRepository.findAuthEntityByEmail(email).isPresent();
            if (isAuthExists) {
                throw new AuthExistsException(String.format("Auth with email - %s exists", email));
            }

            String encodedPassword = passwordEncoder.encode(password);
            LocalDateTime createdAt = LocalDateTime.now();
            AuthEntity createdAuth = authMapper.domainToEntity(AuthDomain.createAuthModel(email, encodedPassword, Role.USER, true, false, createdAt, LocalDateTime.now()));
            authRepository.save(createdAuth);
            log.info("Saved new auth, id - {}", createdAuth.getId());

            String refreshTokenText = saveNewRefreshToken(createdAuth.getId(), createdAuth.getEmail());
            String accessToken = generateAccessToken(email, createdAuth.getId());

            AuthCreatedEventModel eventModel = AuthCreatedEventModel.builder()
                    .email(email)
                    .createdAt(createdAt)
                    .build();
            newAuthEventKafkaTemplate.send("new_auth", createdAuth.getId(), eventModel);

            return TokensDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshTokenText)
                    .build();
        } catch (AuthExistsException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while registering user: {}", e.getMessage());
            throw new RuntimeException(String.format("Internal server error: %s", e.getMessage()));
        }
    }

    @Transactional
    public TokensDTO rotateToken(String refreshTokenText) {
        try {
            if (refreshTokenText == null || refreshTokenText.isBlank()) {
                throw new IllegalArgumentException("Token is null or blank");
            }

            String[] splitToken = refreshTokenText.split("\\.");
            if (splitToken.length != 2) {
                throw new MalformedTokenException("Token is malformed.");
            }
            String tokenId = splitToken[0];
            String tokenSecret = splitToken[1];

            Optional<RefreshToken> optionalRefreshToken;
            try {
                UUID tokenUuid = UUID.fromString(tokenId);
                optionalRefreshToken = refreshTokenRepository.findRefreshTokenById(tokenUuid);
            } catch (IllegalArgumentException e) {
                throw new RefreshTokenNotFoundException("Token id is wrong");
            }

            if (optionalRefreshToken.isEmpty()) {
                throw new RefreshTokenNotFoundException("Refresh token not found");
            }

            RefreshTokenDomain refreshToken = refreshTokenMapper.entityToDomain(optionalRefreshToken.get());

            if (!passwordEncoder.matches(tokenSecret, refreshToken.getTokenHash())) {
                throw new RefreshTokenNotFoundException("Invalid token");
            }

            if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RefreshTokenExpiredException("Token expired");
            }

            if (refreshToken.getRevoked()) {
                log.warn("TOKEN INCIDENT: userId = {}, tokenId = {}, email = {}", refreshToken.getUserId(), refreshToken.getId(), refreshToken.getEmail());
                revokeAllUsersTokens(refreshToken.getUserId());
                throw new TokenStoleException("Token is reused! Please re-login in your account");
            }

            refreshToken.revokeToken();
            refreshTokenRepository.save(refreshTokenMapper.domainToEntity(refreshToken));

            String newRefreshToken = saveNewRefreshToken(refreshToken.getUserId(), refreshToken.getEmail());
            String accessToken = generateAccessToken(refreshToken.getEmail(), refreshToken.getUserId());

            return TokensDTO.builder()
                    .refreshToken(newRefreshToken)
                    .accessToken(accessToken)
                    .build();
        } catch (TokenStoleException | RefreshTokenExpiredException | RefreshTokenNotFoundException |
                 MalformedTokenException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while rotating token: {}", e.getMessage());
            throw new RuntimeException(String.format("Internal server error: %s", e.getMessage()));
        }
    }

    @Transactional
    protected void revokeAllUsersTokens(UUID userId) {
        List<RefreshTokenDomain> refreshTokensByUserId = refreshTokenRepository.findRefreshTokensByUserId(userId).stream().map(refreshTokenMapper::entityToDomain).toList();
        refreshTokensByUserId.forEach(RefreshTokenDomain::revokeToken);
        refreshTokenRepository.saveAll(refreshTokensByUserId.stream().map(refreshTokenMapper::domainToEntity).toList());
    }

    private String saveNewRefreshToken(UUID userId, String email) {
        try {
            String tokenSecret = UUID.randomUUID().toString();
            RefreshTokenDomain savedToken = RefreshTokenDomain.createRefreshToken(userId, email, passwordEncoder.encode(tokenSecret), LocalDateTime.now(), LocalDateTime.now().plusDays(30));
            refreshTokenRepository.save(refreshTokenMapper.domainToEntity(savedToken));
            return savedToken.getId() + "." + tokenSecret;
        } catch (Exception e) {
            log.error("Error saving refresh token: {}", e.getMessage());
            throw new RuntimeException(String.format("Internal server error: %s", e.getMessage()));
        }
    }

    public void updateAuthModel(String email, UpdateType updateType) {
        try {
            Optional<AuthEntity> authEntityByEmail = authRepository.findAuthEntityByEmail(email);
            if (authEntityByEmail.isEmpty()) {
                throw new AuthNotFoundException(String.format("Auth [%s] not found", email));
            }
            AuthDomain auth = authMapper.entityToDomain(authEntityByEmail.get());

            switch (updateType) {
                case UpdateType.DISABLE -> auth.disable();
                case UpdateType.UNLOCK -> auth.unlock();
                case UpdateType.ENABLE -> auth.enable();
                case UpdateType.LOCK -> auth.lock();
                default -> throw new AuthArgumentException("Unknown type of update");
            }

            authRepository.save(authMapper.domainToEntity(auth));
        } catch (AuthNotFoundException | AuthArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public TokensDTO login(LoginDTO loginDTO) {
        try {
            String email = loginDTO.getEmail();
            String password = loginDTO.getPassword();

            AuthDomain auth = authMapper.entityToDomain(authRepository.findAuthEntityByEmail(email).orElseThrow(() -> new AuthNotFoundException(String.format("Auth %s not found", email))));
            String passwordHash = auth.getPasswordHash();

            boolean isPasswordCorrect = passwordEncoder.matches(password, passwordHash);
            if (!isPasswordCorrect) {
                throw new LoginException("Password is incorrect");
            }

            UUID userId = auth.getId();
            String refreshToken = saveNewRefreshToken(userId, email);
            String accessToken = generateAccessToken(email, userId);
            auth.updateLastLoginAt(LocalDateTime.now());

            authRepository.save(authMapper.domainToEntity(auth));
            return TokensDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (LoginException | AuthNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
