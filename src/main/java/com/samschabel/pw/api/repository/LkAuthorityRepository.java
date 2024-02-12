package com.samschabel.pw.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samschabel.pw.api.entity.LkAuthorityEntity;

@Repository
public interface LkAuthorityRepository extends JpaRepository<LkAuthorityEntity, Long>{

}
