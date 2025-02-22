package com.swd392.skincare_products_sales_system.model;

import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
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
    Long id; // Mã đơn hàng

    Double totalAmount; // Tổng giá trị đơn hàng (bao gồm phí vận chuyển, giảm giá)
    String orderInfo; // Thông tin đơn hàng (ví dụ: "Thanh toán đơn hàng #12345")
    String username; // Tên người dùng
    LocalDateTime orderDate; // Ngày tạo đơn hàng
    @Enumerated(EnumType.STRING)
    Status status; // Trạng thái đơn hàng (ví dụ: "Đang xử lý", "Đã giao", "Đã hủy")

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    Address address;

    // Phương thức thanh toán
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod; // Phương thức thanh toán (COD, Thẻ ATM, Thẻ quốc tế, v.v.)
    Double shippingFee; // Phí vận chuyển

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<OrderItem> orderItems;  // Danh sách các sản phẩm trong đơn hàng

    // Các thông tin khác
    String discountCode; // Mã giảm giá (nếu có)
    Double discountAmount; // Số tiền giảm giá
    String deliveryTime; // Thời gian giao hàng dự kiến

}
