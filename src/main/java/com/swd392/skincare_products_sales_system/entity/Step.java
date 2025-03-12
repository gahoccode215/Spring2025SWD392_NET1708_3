package com.swd392.skincare_products_sales_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.RoutineStatusEnum;
import com.swd392.skincare_products_sales_system.enums.TimeOfDayStatus;
import com.swd392.skincare_products_sales_system.entity.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tbl_step")
public class Step extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    Integer stepNumber;

    @Enumerated(EnumType.STRING)
    TimeOfDayStatus timeOfDay;

    @Column
    @NotNull(message = "Action cannot not be null")
    String action;

    @Column
    @NotNull(message = "Description cannot be null")
    String description;

    @Enumerated(EnumType.STRING)
    RoutineStatusEnum routineStatus;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "daily_routine_id")
    DailyRoutine dailyRoutine;

}
