package com.swd392.skincare_products_sales_system.model;

import com.swd392.skincare_products_sales_system.enums.SkinType;
import com.swd392.skincare_products_sales_system.model.authentication.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_result")
public class Result extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "SkinType cannot be null")
    SkinType skinType;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
     Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}
