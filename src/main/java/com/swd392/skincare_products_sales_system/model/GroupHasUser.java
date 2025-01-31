package com.swd392.skincare_products_sales_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_group_has_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupHasUser extends AbstractEntity<Integer> {

    @ManyToOne
    @JoinColumn(name = "group_id")
    Group group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;
}
