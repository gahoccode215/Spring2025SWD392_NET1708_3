package com.swd392.skincare_products_sales_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.DiscountType;
import com.swd392.skincare_products_sales_system.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tbl_voucher")
public class Voucher extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "code")
    String code;

    @Enumerated(EnumType.STRING)
    DiscountType discountType;

    @Column(name = "min_order_value")
    Double minOrderValue;

    @Column(name = "description")
    String description;

    @Column(name = "point")
    Integer point;

    @Enumerated(EnumType.STRING)
    Status status;

    @Column(name = "quantity")
    Integer quantity;

    @ManyToMany(mappedBy = "vouchers")
    @JsonIgnore
    Set<User> users;

}