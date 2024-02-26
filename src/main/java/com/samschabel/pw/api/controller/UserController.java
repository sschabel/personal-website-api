package com.samschabel.pw.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        try {
            Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
            if (authenticationResponse.isAuthenticated()) {
                LoginResponse response = new LoginResponse(jwtService.generateToken(request.getUsername()),
                        null, setupUserFromAuthentication(authenticationResponse));
                return ResponseEntity.ok(response);
            } else {
                log.error("Invalid login for username " + request.getUsername());
                return ResponseEntity.ok(new LoginResponse(null, "Invalid login.", null));
            }
        } catch (BadCredentialsException badCredentialsException) {
            userService.incrementLoginAttempts(request.getUsername());
            return ResponseEntity.ok(new LoginResponse(null, "Invalid username or password", null));
        } catch (DisabledException disabledException) {
            log.info("Disabled user, " + request.getUsername() + " attempted to login.", disabledException);
            return ResponseEntity.ok(new LoginResponse(null, "Your account is disabled", null));
        } catch (LockedException lockedException) {
            log.info("Locked user, " + request.getUsername() + " attempted to login.", lockedException);
            return ResponseEntity.ok(new LoginResponse(null, "Your account is locked", null));
        } catch (AuthenticationException authenticationException) {
            log.info("Error occurred during login attempt for user, " + request.getUsername() + " .", authenticationException);
            return ResponseEntity.ok(new LoginResponse(null, "An error occurred during login", null));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = setupUserFromAuthentication(authentication);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/csrf")
    @ResponseStatus(value = HttpStatus.OK)
    public void csrfToken() {
        // a GET HTTP method in order to have Spring Security set the XSRF Token as a
        // cookie
        // so future HTTP requests will be able to use that XSRF Token (X-XSRF-TOKEN) in
        // a HTTP Header
    }

    private User setupUserFromAuthentication(Authentication authentication) {
        User user = new User();
        user.setUsername(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
        List<AuthorityEnum> authorities = new ArrayList();
        authentication.getAuthorities()
                .forEach(authority -> authorities.add(AuthorityEnum.valueOf(authority.getAuthority())));
        user.setAuthorities(authorities);
        return user;
    }

}
