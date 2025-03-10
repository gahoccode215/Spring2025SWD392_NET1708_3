package com.swd392.skincare_products_sales_system.entity;

import com.swd392.skincare_products_sales_system.enums.RoutineStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tbl_daily_routine")
public class DailyRoutine extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    @FutureOrPresent
    LocalDate date;

    @Enumerated(EnumType.STRING)
    RoutineStatusEnum routineStatus;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    Routine routine;

    @OneToMany(mappedBy = "dailyRoutine", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Step> steps;


}
