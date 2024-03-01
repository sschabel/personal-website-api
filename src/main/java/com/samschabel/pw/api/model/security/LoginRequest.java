package com.samschabel.pw.api.model.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @Email
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String reCaptchaToken;
    
}
