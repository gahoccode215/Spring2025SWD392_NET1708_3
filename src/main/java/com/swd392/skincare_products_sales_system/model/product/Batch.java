package com.swd392.skincare_products_sales_system.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.model.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_batch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Batch extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "batch_code", unique = true)
    String batchCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    Product product;

    @Column(name = "quantity")
    Integer quantity;

    @Column(name = "manufacture_date", nullable = false)
    LocalDate manufactureDate;

    @Column(name = "expiration_date", nullable = false)
    LocalDate expirationDate;


    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (manufactureDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày sản xuất không thể lớn hơn ngày hiện tại.");
        }
        if (manufactureDate.isAfter(expirationDate)) {
            throw new IllegalArgumentException("Ngày sản xuất phải trước ngày hết hạn.");
        }
        if (expirationDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày hết hạn không thể nhỏ hơn ngày hiện tại.");
        }
    }

}
