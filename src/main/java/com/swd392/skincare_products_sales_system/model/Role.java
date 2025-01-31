package com.swd392.skincare_products_sales_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Entity
@Table(name = "tbl_role")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends AbstractEntity<Integer>{
    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    
}
