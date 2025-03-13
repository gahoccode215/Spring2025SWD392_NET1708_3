package com.swd392.skincare_products_sales_system.enity.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.BookingStatus;
import com.swd392.skincare_products_sales_system.enums.PaymentStatus;
import com.swd392.skincare_products_sales_system.enums.SkinType;
import com.swd392.skincare_products_sales_system.enity.AbstractEntity;
import com.swd392.skincare_products_sales_system.enity.SkincareService;
import com.swd392.skincare_products_sales_system.enity.User;
import com.swd392.skincare_products_sales_system.enity.routine.Routine;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

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
    LocalDateTime orderDate;

    @Column
    LocalDateTime date;

    @Column
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be a positive number")
    Float price;

    @Column
    String note;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "SkinType cannot be null")
    SkinType skinType;

    @Column
    String allergy;

    @Column
    String skinCondition;

    @Column
    String expertName;

    @Column
    String firstName;

    @Column
    String lastName;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    @Column
    @NotNull(message = "Age cannot be null")
    @Positive(message = "Age must be a positive number")
    Integer age;

    @Column(name = "response", columnDefinition = "TEXT")
    String response;

    @Column
    String serviceName;

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

    @OneToMany(mappedBy = "bookingOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    List<ImageSkin> imageSkins;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @OneToMany(mappedBy = "bookingOrder")
    @JsonIgnore
    List<ProcessBookingOrder> processBookingOrders;
}
