package com.swd392.skincare_products_sales_system.model.quiz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.SkinType;
import com.swd392.skincare_products_sales_system.model.AbstractEntity;
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
@Table(name = "tbl_answer")
public class Answer extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(nullable = false)
    @NotNull(message = "Answer cannot be null")
    String answerText;

    @Enumerated(EnumType.STRING)
    SkinType skinType;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore
    Question question;

}
