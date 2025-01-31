package com.swd392.skincare_products_sales_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "tbl_permission")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends AbstractEntity<Integer>{
    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @OneToMany(mappedBy = "permission")
    Set<RoleHasPermission> permissions = new HashSet<>();

}
