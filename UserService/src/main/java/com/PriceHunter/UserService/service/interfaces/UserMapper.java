package com.PriceHunter.UserService.service.interfaces;

import com.PriceHunter.UserService.models.domain.UserDomain;
import com.PriceHunter.UserService.models.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User domainToEntity(UserDomain userDomain);
    UserDomain entityToDomain(User user);
}
