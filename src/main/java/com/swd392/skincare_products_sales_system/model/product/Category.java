package com.swd392.skincare_products_sales_system.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@ToString
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_category")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "name")
    String name;

    @Column(name = "slug", unique = true)
    String slug;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status = Status.ACTIVE;

    @Column(name = "thumbnail")
    String thumbnail;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    @JsonIgnore
    List<Product> products;
}
