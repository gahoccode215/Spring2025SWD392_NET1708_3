package com.swd392.skincare_products_sales_system.model;

import com.swd392.skincare_products_sales_system.enums.Status;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    String voucherName;

    @Column(nullable = false)
    String voucherCode;

    @Column(nullable = false)
    LocalDate startDate;

    @Column(nullable = false)
    LocalDate endDate;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    Integer point;

    @Column(nullable = false)
    Float discountAmount;

    @Enumerated(EnumType.STRING)
    Status status;

    @ManyToMany(mappedBy = "vouchers")
    Set<User> users;

}