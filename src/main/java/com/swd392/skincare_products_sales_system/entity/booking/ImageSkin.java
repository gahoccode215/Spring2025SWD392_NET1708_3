package com.swd392.skincare_products_sales_system.entity.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.entity.AbstractEntity;
import com.swd392.skincare_products_sales_system.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tbl_image_skin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class ImageSkin extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String image;

    @ManyToOne
    @JoinColumn(name = "bookingOrder_id", nullable = false)
    @JsonIgnore
    BookingOrder bookingOrder;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;
}
