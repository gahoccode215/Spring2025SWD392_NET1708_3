package com.swd392.skincare_products_sales_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_group")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Group extends AbstractEntity<Integer>{
    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;
}
