package com.swd392.skincare_products_sales_system.model;

import com.swd392.skincare_products_sales_system.enums.RoutineStatusEnum;
import com.swd392.skincare_products_sales_system.model.product.Product;
import jakarta.persistence.*;
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

    @Column
    String timeOfDay;

    @Column
    String action;

    @Column
    String description;

    @Column
    String note;

    @Enumerated(EnumType.STRING)
    RoutineStatusEnum routineStauts;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "daily_routine_id")
    DailyRoutine dailyRoutine;

}
