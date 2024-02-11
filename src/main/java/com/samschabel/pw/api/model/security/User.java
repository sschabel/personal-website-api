package com.samschabel.pw.api.model.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private List<GrantedAuthority> authorities;
    private String firstName;
    private String lastName;
    private String username;
}
