package com.swd392.skincare_products_sales_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@ToString
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_cart_item")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    Cart cart;  // Liên kết sản phẩm với giỏ hàng

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;  // Liên kết với sản phẩm

    Integer quantity;  // Số lượng sản phẩm
    Double price;  // Giá sản phẩm tại thời điểm thêm vào giỏ

    public Double getTotalPrice() {
        return this.quantity * this.price;
    }
}
