package com.PriceHunter.AuthService.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class LoginDTO {
    @Email(message = "email is not valid")
    @NotBlank(message = "email field cant be blank")
    @NotNull(message = "email field is required")
    private String email;

    @NotNull(message = "password field is required")
    @NotBlank(message = "password field cant be blank")
    private String password;
}
