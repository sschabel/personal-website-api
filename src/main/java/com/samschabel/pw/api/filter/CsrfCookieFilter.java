package com.samschabel.pw.api.filter;

import java.io.IOException;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* Taken from Spring Security 6 documentation (https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript)
 * This filter is needed because Spring Security rejects our 1st POST request because there is so X-XSRF-TOKEN
 * in the HTTP request. This will generate a new one and set it as a cookie for Angular to use in future HTTP requests.
 */
public class CsrfCookieFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
        
        //CsrfToken csrfToken = (CsrfToken) request.getAttribute("X-XSRF-TOKEN");
		CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
		// Render the token value to a cookie by causing the deferred token to be loaded
		csrfToken.getToken();

		filterChain.doFilter(request, response);
	}
}