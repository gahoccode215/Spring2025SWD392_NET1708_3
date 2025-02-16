package com.swd392.skincare_products_sales_system.model;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    Product product;

    Integer quantity;
    LocalDate manufactureDate;
    LocalDate expirationDate;


    // Kiểm tra xem lô có còn sản phẩm không
    public boolean isAvailable() {
        return quantity > 0;
    }
}
