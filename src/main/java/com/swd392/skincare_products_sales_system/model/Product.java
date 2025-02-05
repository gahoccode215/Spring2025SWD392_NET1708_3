package com.swd392.skincare_products_sales_system.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_product")
public class Product extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "name")
    String name;

    @Column(name = "price")
    double price;

    @Column(name = "description")
    String description;

    @Column(name = "slug", nullable = false, unique = true)
    String slug;

    @Column(name = "stock")
    long stock;

    @Column(name = "brand")
    String brand;

    @Column(name = "thumbnail")
    String thumbnail;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}
