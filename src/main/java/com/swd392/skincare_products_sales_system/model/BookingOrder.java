package com.swd392.skincare_products_sales_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.BookingStatus;
import com.swd392.skincare_products_sales_system.enums.SkinType;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.time.LocalDate;
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

    @Column
    Integer age;

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
}
