package com.swd392.skincare_products_sales_system.entity.authentication;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swd392.skincare_products_sales_system.entity.AbstractEntity;
import com.swd392.skincare_products_sales_system.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tbl_role")
public class Role extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @JsonManagedReference
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    Set<User> users;

}
