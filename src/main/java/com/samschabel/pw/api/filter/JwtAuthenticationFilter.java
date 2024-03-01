package com.samschabel.pw.api.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.samschabel.pw.api.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER); 
        String token = null; 
        String username = null; 
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_TOKEN_PREFIX)) { 
            token = authorizationHeader.substring(7); 
            username = jwtService.extractUsername(token); 
        }
  
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { 
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); 
            if (jwtService.validateToken(token, userDetails)) { 
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); 
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); 
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); 
            } 
        }

        filterChain.doFilter(request, response); 
    }

}
