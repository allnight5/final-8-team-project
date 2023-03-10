package com.team.final8teamproject.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class RegenerateTokenResponseDto {
    private String accessToken;

    public RegenerateTokenResponseDto(String accessToken){
        this.accessToken = accessToken;
    }

}
