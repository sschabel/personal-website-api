package com.samschabel.pw.api.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LK_ROLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LkRoleEntity {

    @Id
    Long id;
    String name;
    @OneToMany(mappedBy = "role")
    List<LkAuthorityEntity> authorities;

}
