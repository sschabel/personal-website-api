package com.samschabel.pw.api.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String username;
    String password;
    boolean enabled;
    boolean locked;
    int loginAttempts;

    @JoinTable(
        name = "USER_ROLE",
        joinColumns = @JoinColumn(
                name = "USER_ID",
                referencedColumnName = "id"
        ),
        inverseJoinColumns = @JoinColumn(
                name = "ROLE_ID",
                referencedColumnName = "id"
        )
    )
    @ManyToMany
    List<LkRoleEntity> roles;
}
