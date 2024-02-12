package com.samschabel.pw.api.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.samschabel.pw.api.entity.LkAuthorityEntity;
import com.samschabel.pw.api.entity.LkRoleEntity;

@ActiveProfiles("Integration")
@SpringBootTest
public class LkAuthorityRepository_IT {

    @Autowired
    LkAuthorityRepository cut; // LkAuthorityRepository is our CUT (Class Under Test)
    @Autowired
    LkRoleRepository lkRoleRepository;

    @Test
    void insertNewAuthorityLookup() {
        // given
        Optional<LkRoleEntity> roleEntity = lkRoleRepository.findByName("ADMIN");
        LkAuthorityEntity entity = new LkAuthorityEntity(1L, "ADMIN", roleEntity.get());
        // when
        cut.save(entity);
        // then
        // Authority should be added to the database
    }

}
