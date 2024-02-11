package com.samschabel.pw.api.service.security;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import com.samschabel.pw.api.model.security.UserDetailsImpl;

@ActiveProfiles("Integration")
@SpringBootTest
public class UserDetailsServiceImpl_IT {

    @Autowired
    UserDetailsServiceImpl cut; // UserDetailsServiceImpl is our CUT (Class Under Test)

    @Test
    void insertNewUser() {
        // given
        String username = "";
        String password = "";
        
        List<GrantedAuthority> authorities = new ArrayList();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        UserDetailsImpl user = new UserDetailsImpl();
        user.setEnabled(true);
        user.setUsername(username);
        user.setPassword(password);
        user.setAuthorities(authorities);
        // when
        cut.addUser(user);
        // then
        // user should be added to the database
    }


}
