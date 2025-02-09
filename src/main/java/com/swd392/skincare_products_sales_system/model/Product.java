package com.swd392.skincare_products_sales_system.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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

    @Column(name = "feature")
    String feature; // Công dụng: Dưỡng ẩm, trị mụn, chống lão hóa

    @Column(name = "price")
    Double price;

    @Column(name = "slug")
    String slug;

    @Column(name = "quantity_per_pack")
    Integer quantityPerPack; // Số lượng theo lô

    @Column(name = "product_code")
    String productCode; // Mã sản phẩm

    @Column(name = "description")
    String description; // Mô tả sản phẩm

    @Column(name = "thumbnail")
    String thumbnail; // URL hình ảnh

    @Column(name = "usage_instruction")
    String usageInstruction; // Hướng dẫn sử dụng

    @Column(name = "expiry_date")
    String expiryDate; // Ngày hết hạn

    @ManyToOne
    @JoinColumn(name = "brand_id")
    Brand brand;

    @ManyToOne
    @JoinColumn(name = "origin_id")
    Origin origin;

    @ManyToOne
    @JoinColumn(name = "skin_type_id")
    SkinType skinType;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbl_product_has_feature",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    Set<Feature> features;
}
