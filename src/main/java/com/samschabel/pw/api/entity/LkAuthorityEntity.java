package com.samschabel.pw.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LK_AUTHORITY")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LkAuthorityEntity {

    @Id
    Long id;
    String name;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "id")
    LkRoleEntity role;

}
