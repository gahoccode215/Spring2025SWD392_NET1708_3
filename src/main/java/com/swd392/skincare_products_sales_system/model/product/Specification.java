package com.swd392.skincare_products_sales_system.model.product;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_specification")
public class Specification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "origin")
    String origin;

    @Column(name = "brand_origin")
    String brandOrigin;

    @Column(name = "manufacturing_location")
    String manufacturingLocation;

    @Column(name = "skin_type")
    String skinType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;
}
