package com.PriceHunter.AuthService.services.interfaces;

import com.PriceHunter.AuthService.models.RefreshToken;
import com.PriceHunter.AuthService.models.domain.RefreshTokenDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    RefreshToken domainToEntity(RefreshTokenDomain refreshTokenDomain);
}
