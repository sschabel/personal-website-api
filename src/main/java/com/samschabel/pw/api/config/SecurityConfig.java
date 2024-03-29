package com.samschabel.pw.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.samschabel.pw.api.filter.CsrfCookieFilter;
import com.samschabel.pw.api.filter.JwtAuthenticationFilter;
import com.samschabel.pw.api.handler.SpaCsrfTokenRequestHandler;
import com.samschabel.pw.api.model.security.AuthorityEnum;
import com.samschabel.pw.api.repository.LkRoleRepository;
import com.samschabel.pw.api.repository.UserRepository;
import com.samschabel.pw.api.service.JwtService;
import com.samschabel.pw.api.service.UserDetailsServiceImpl;

import jakarta.servlet.http.Cookie;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    JwtService jwtService;
    @Autowired
    LkRoleRepository lkRoleRepository;
    @Autowired
    UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // use CookieCsrfTokenRepository in order to set path to the root instead of
        // /api
        CookieCsrfTokenRepository cookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        cookieCsrfTokenRepository.setCookiePath("/");
        cookieCsrfTokenRepository.setCookieCustomizer((customizer) -> customizer.maxAge(1800));
        // create cookie in order to set the path to the root instead of the /api path
        // and then clear it on logout
        Cookie bearerTokenCookie = new Cookie("pw-api_bearer", null);
        bearerTokenCookie.setMaxAge(0);
        bearerTokenCookie.setPath("/");
        return http
                .headers((headers) -> headers.frameOptions((options) -> options.sameOrigin()))
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(cookieCsrfTokenRepository)
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/csrf", "/login", "/h2-console/**", "/blog/articles", "/recaptcha/**").permitAll()
                        .requestMatchers("/blog/**", "/user").hasAuthority(AuthorityEnum.ADMIN.toString())
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout((logout) -> logout.addLogoutHandler(new CookieClearingLogoutHandler(bearerTokenCookie))
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, userDetailsService());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(passwordEncoder(), userRepository, lkRoleRepository);
    }

}
