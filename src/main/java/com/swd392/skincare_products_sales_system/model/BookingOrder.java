package com.swd392.skincare_products_sales_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.BookingStatus;
import com.swd392.skincare_products_sales_system.enums.SkinType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_bookingOrder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class BookingOrder extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    LocalDate orderDate;

    @Column
    Float price;

    @Column
    String note;

    @Enumerated(EnumType.STRING)
    SkinType skinType;

    @Column
    String image;

    @Column
    String allergy;

    @Column
    String skinCondition;

    @Column
    String expertName;

    @Enumerated(EnumType.STRING)
    BookingStatus status;

    @ManyToOne
    @JoinColumn(name = "skincareService_id", nullable = false)
    @JsonIgnore
    SkincareService skincareService;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;
}
