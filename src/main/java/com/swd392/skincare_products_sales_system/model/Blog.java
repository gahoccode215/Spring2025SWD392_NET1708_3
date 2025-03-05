package com.swd392.skincare_products_sales_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_blog")
public class Blog extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Lob
    @NotNull(message = "Content cannot be null")
    @Size(min = 20)
    String content;

    @Column
    @NotNull(message = "BlogName cannot be null")
    @Size(min = 5)
    String blogName;

    @Column
    @NotNull(message = "Description cannot be null")
    @Size(min = 5)
    String description;

    @Column
    @NotNull(message = "Image cannot be null")
    String image;

    @Enumerated(EnumType.STRING)
    Status status;

    @Column
    LocalDateTime date;

    @Column
    String createdBy;


}
