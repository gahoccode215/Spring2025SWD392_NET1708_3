package com.swd392.skincare_products_sales_system.model.authentication;

import com.swd392.skincare_products_sales_system.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_otp")
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key cho bảng OTP

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // Liên kết OTP với người dùng

    @Column(name = "otp")
    private String otp; // OTP được tạo

    @Column(name = "expiration_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationTime; // Thời gian hết hạn của OTP

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date(); // Thời gian tạo OTP
}
