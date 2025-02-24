package com.swd392.skincare_products_sales_system.model;

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

    @Column(nullable = false)
    @Size(min = 5, message = "VoucherName must have at least 5 character")
    String voucherName;

    @Column(nullable = false)
    @Size(min = 5, max = 100, message = "VoucherName must have at least 5 character")
    String voucherCode;

    @Column(nullable = false)
    @FutureOrPresent
    LocalDate startDate;

    @Column(nullable = false)
    @FutureOrPresent
    LocalDate endDate;

    @Column(nullable = false)
    @Size(min = 5, message = "Description must have at least 5 character")
    String description;

    @Column(nullable = false)
    @DecimalMin(value = "0", inclusive = true, message = "Point must be greater than or equal to 0")
    Integer point;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "DiscountAmount must be greater than or equal to 0")
    Float discountAmount;

    @Enumerated(EnumType.STRING)
    Status status;

    @ManyToMany(mappedBy = "vouchers")
    Set<User> users;

}