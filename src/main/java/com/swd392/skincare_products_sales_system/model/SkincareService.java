package com.swd392.skincare_products_sales_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.Status;
import jakarta.persistence.*;
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
    String serviceName;

    @Enumerated(EnumType.STRING)
    Status status;

    @Column(nullable = false)
    Float price;

    @Column
    String description;

    @OneToMany(mappedBy = "skincareService")
    @JsonIgnore
    List<BookingOrder> bookingOrders;

}
