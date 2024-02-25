package com.samschabel.pw.api.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String bearerToken;
    private String errorMessage;
    private User user;
}
