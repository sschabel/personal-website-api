package com.samschabel.pw.api.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.samschabel.pw.api.entity.LkAuthorityEntity;
import com.samschabel.pw.api.entity.LkRoleEntity;

@ActiveProfiles("Integration")

@SpringBootTest
public class LkRoleRepository_IT {

    @Autowired
    LkRoleRepository cut; // LkRoleRepository is our CUT (Class Under Test)

    @Test
    void insertNewRoleLookup() {
        // given
        LkRoleEntity roleEntity = new LkRoleEntity(1L, "ADMIN", null);
        // when
        cut.save(roleEntity);
        // then
        // Role should be added to the database
    }

}
