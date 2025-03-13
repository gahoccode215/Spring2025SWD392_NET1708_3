package com.swd392.skincare_products_sales_system.enity.product;

import com.swd392.skincare_products_sales_system.enity.AbstractEntity;
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

    @Column(name = "batch_code", unique = true, nullable = false)
    String batchCode; // Mã lô hàng

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    Product product; // Mỗi batch thuộc về một sản phẩm

    @Column(name = "quantity", nullable = false)
    Integer quantity; // Số lượng nhập

    @Column(name = "import_price", nullable = false)
    Double importPrice; // Giá nhập hàng

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
