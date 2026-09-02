package com.PriceHunter.AuthService.models.dto;

import com.PriceHunter.AuthService.models.enums.UpdateType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class UpdateAuthDTO {
    @Email(message = "Email isn`t valid")
    @NotBlank(message = "Email is required")
    private String email;
    @NotNull(message = "Update type is required")
    private UpdateType updateType;
}
