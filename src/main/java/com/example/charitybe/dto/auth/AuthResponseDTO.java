package com.example.charitybe.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO<T> {
    private String accessToken;
    private T userInfo;

}

