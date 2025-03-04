package com.swd392.skincare_products_sales_system.model;


import com.swd392.skincare_products_sales_system.enums.RoutineStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "tbl_routine")
public class Routine extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    LocalDate startDate;

    @Column
    LocalDate endDate;

    @Column
    @NotNull(message = "Routine cannot be null")
    String routineName;

    @Column
    String description;

    @Column
    String expertId;

    @Enumerated(EnumType.STRING)
    @Column(name = "routine_status")
    RoutineStatusEnum routineStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "routine")
    List<DailyRoutine> dailyRoutines;

    @OneToOne(mappedBy = "routine")
    BookingOrder bookingOrder;
}
