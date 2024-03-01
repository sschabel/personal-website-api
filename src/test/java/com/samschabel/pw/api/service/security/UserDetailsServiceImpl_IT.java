package com.samschabel.pw.api.service.security;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.samschabel.pw.api.TestNameUtils;
import com.samschabel.pw.api.model.security.RoleEnum;
import com.samschabel.pw.api.model.security.UserDetailsImpl;
import com.samschabel.pw.api.service.UserDetailsServiceImpl;

@ActiveProfiles("Integration")
@SpringBootTest
@DisplayNameGeneration(value = TestNameUtils.class)
public class UserDetailsServiceImpl_IT {

    @Autowired
    UserDetailsServiceImpl cut; // UserDetailsServiceImpl is our CUT (Class Under Test)

    @Test
    void insertNewUser() {
        // given
        String username = "";
        String password = "";
        
        List<RoleEnum> roles = new ArrayList();
        roles.add(RoleEnum.ADMIN);
        UserDetailsImpl user = new UserDetailsImpl();
        user.setEnabled(true);
        user.setLocked(false);
        user.setUsername(username);
        user.setPassword(password);
        // when
        cut.addUser(user, roles);
        // then
        // user should be added to the database
    }


}
