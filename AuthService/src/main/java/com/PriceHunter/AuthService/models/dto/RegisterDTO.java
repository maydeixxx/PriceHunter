package com.PriceHunter.AuthService.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
public class RegisterDTO {
    @Email(message = "Email isn`t valid")
    @NotBlank(message = "Email is required")
    private String email;
    @Min(value = 8, message = "Min. length is 8 symbols")
    @NotBlank(message = "Password is required")
    private String password;
}
