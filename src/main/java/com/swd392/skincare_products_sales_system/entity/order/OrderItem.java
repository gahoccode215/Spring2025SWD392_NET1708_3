package com.swd392.skincare_products_sales_system.entity.order;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.entity.product.Batch;
import com.swd392.skincare_products_sales_system.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "tbl_order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    List<Batch> batches;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @Column(name = "quantity")
    Integer quantity;

    @Column(name = "price")
    Double price;

    @Column(name = "total_price")
    Double totalPrice;

    @Column(name = "is_feedback")
    Boolean isFeedback;

    public Double calculateTotalPrice() {
        return this.price * this.quantity;
    }
}
