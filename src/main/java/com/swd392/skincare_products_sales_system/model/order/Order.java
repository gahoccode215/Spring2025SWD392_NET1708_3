package com.swd392.skincare_products_sales_system.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.OrderStatus;
import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.enums.PaymentStatus;
import com.swd392.skincare_products_sales_system.model.Address;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Double totalAmount;
    String orderInfo;
    String username;
    LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    @JsonIgnore
    Address address;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;


    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;
    Double shippingFee;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    List<OrderItem> orderItems;

    String discountCode;
    Double discountAmount;
    String deliveryTime;

}
