package com.swd392.skincare_products_sales_system.entity.routine;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.RoutineStatusEnum;
import com.swd392.skincare_products_sales_system.entity.AbstractEntity;
import com.swd392.skincare_products_sales_system.entity.booking.BookingOrder;
import com.swd392.skincare_products_sales_system.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
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
    @JsonIgnore
    User user;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DailyRoutine> dailyRoutines = new ArrayList<>();

    @OneToOne(mappedBy = "routine")
            @JsonIgnore
    BookingOrder bookingOrder;
}
