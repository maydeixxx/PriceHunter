package com.PriceHunter.AuthService.models.dto;

import com.PriceHunter.AuthService.models.enums.UpdateType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateAuthDTO {
    private String email;
    private UpdateType updateType;
}
