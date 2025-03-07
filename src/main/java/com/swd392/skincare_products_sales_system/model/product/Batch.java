package com.swd392.skincare_products_sales_system.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.AbstractEntity;
import com.swd392.skincare_products_sales_system.model.order.OrderItem;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "tbl_batch")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "batch_code", unique = true)
    String batchCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_item_id")
    OrderItem orderItem;

    @Column(name = "quantity")
    Integer quantity;

    @Column(name = "manufacture_date", nullable = false)
    LocalDate manufactureDate;

    @Column(name = "expiration_date", nullable = false)
    LocalDate expirationDate;


    @Column(name = "created_at", length = 255)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Date createdAt;

    @Column(name = "updated_at", length = 255)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    Date updatedAt;

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (manufactureDate.isAfter(LocalDate.now())) {
            throw new AppException(ErrorCode.MANUFACTURE_DATE_CAN_NOT_AFTER_TODAY);
        }
        if (manufactureDate.isAfter(expirationDate)) {
            throw new AppException(ErrorCode.MANUFACTURE_DATE_CAN_NOT_AFTER_EXPIRATION_DATE);
        }
        if (expirationDate.isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.EXPIRATION_DATE_CAN_NOT_BEFORE_TODAY);
        }
    }

}
