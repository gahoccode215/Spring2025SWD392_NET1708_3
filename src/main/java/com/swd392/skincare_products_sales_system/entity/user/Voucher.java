package com.swd392.skincare_products_sales_system.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.DiscountType;
import com.swd392.skincare_products_sales_system.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tbl_voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "code")
    String code;

    @Enumerated(EnumType.STRING)
    DiscountType discountType;

    @Column(name = "discount")
    Integer discount;

    @Column(name = "min_order_value")
    Double minOrderValue;

    @Column(name = "description")
    String description;

    @Column(name = "point")
    Integer point;

    @Enumerated(EnumType.STRING)
    Status status;

    @ManyToMany(mappedBy = "vouchers", cascade = CascadeType.ALL)
    @JsonIgnore
    List<User> users;

    public void addUser(User obj){
        users.add(obj);
    }

}