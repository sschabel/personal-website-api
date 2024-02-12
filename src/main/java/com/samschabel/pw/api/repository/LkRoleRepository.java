package com.samschabel.pw.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samschabel.pw.api.entity.LkRoleEntity;
import java.util.Optional;


@Repository
public interface LkRoleRepository extends JpaRepository<LkRoleEntity, Long>{

    Optional<LkRoleEntity> findByName(String name);
}
