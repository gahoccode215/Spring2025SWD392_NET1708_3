package com.swd392.skincare_products_sales_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.entity.booking.BookingOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@ToString
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_skincareService")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkincareService extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    @NotNull(message = "ServiceName cannot be null")
    String serviceName;

    @Enumerated(EnumType.STRING)
    Status status;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
    @Column
    Float price;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;
    @OneToMany(mappedBy = "skincareService")
    @JsonIgnore
    List<BookingOrder> bookingOrders;

}
