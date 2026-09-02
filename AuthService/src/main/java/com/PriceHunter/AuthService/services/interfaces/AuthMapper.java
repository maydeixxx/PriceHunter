package com.PriceHunter.AuthService.services.interfaces;

import com.PriceHunter.AuthService.models.AuthEntity;
import com.PriceHunter.AuthService.models.domain.AuthDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthEntity domainToEntity(AuthDomain domain);

    AuthDomain entityToDomain(AuthEntity entity);
}
