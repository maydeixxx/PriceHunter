package com.PriceHunter.AuthService.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
public class RegisterDTO {
    @Email(message = "Email isn`t valid")
    @NotBlank(message = "Email is required")
    private String email;
    @Size(min = 8, max = 15, message = "Length of password must be >= 8 and <= 15")
    @NotBlank(message = "Password is required")
    private String password;
}
