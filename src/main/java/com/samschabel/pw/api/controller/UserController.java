package com.samschabel.pw.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.samschabel.pw.api.exception.ApiException;
import com.samschabel.pw.api.model.security.AuthorityEnum;
import com.samschabel.pw.api.model.security.LoginRequest;
import com.samschabel.pw.api.model.security.LoginResponse;
import com.samschabel.pw.api.model.security.NewUserRequest;
import com.samschabel.pw.api.model.security.User;
import com.samschabel.pw.api.model.security.UserDetailsImpl;
import com.samschabel.pw.api.service.security.JwtService;
import com.samschabel.pw.api.service.security.UserDetailsServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userService;

    @PostMapping("/addNewUser")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewUser(@Valid @RequestBody NewUserRequest request) {
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUsername(request.getUsername());
        userDetails.setPassword(request.getPassword());
        userDetails.setEnabled(true);
        userService.addUser(userDetails, request.getRoles());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(request.getUsername(), request.getPassword());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        if (authenticationResponse.isAuthenticated()) {
            User user = new User();
            user.setUsername(request.getUsername());
            List<AuthorityEnum> authorities = new ArrayList();
            authenticationResponse.getAuthorities()
                    .forEach(authority -> authorities.add(AuthorityEnum.valueOf(authority.getAuthority())));
            user.setAuthorities(authorities);
            LoginResponse response = new LoginResponse(jwtService.generateToken(request.getUsername()), user);
            return ResponseEntity.ok(response);
        } else {
            throw new ApiException("Invalid user request.");
        }
    }

    @GetMapping("/csrf")
    @ResponseStatus(value = HttpStatus.OK)
    public void csrfToken() {
        // a GET HTTP method in order to have Spring Security set the XSRF Token as a
        // cookie
        // so future HTTP requests will be able to use that XSRF Token (X-XSRF-TOKEN) in
        // a HTTP Header
    }

}
