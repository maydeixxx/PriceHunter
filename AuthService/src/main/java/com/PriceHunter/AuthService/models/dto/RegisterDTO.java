package com.PriceHunter.AuthService.models.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterDTO {
    private String email;
    private String password;
}
