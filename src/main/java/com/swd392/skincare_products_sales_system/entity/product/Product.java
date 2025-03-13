package com.swd392.skincare_products_sales_system.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.entity.*;
import com.swd392.skincare_products_sales_system.entity.Step;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_product")
public class Product extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "name")
    String name;

    @Column(name = "price")
    Double price;

    @Column(name = "slug")
    String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "thumbnail")
    String thumbnail;

    @Column(name = "size")
    String size;

    @Column(name = "stock")
    Integer stock = 0; // Mặc định tồn kho = 0

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonIgnore
    List<Batch> batches;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonIgnore
    List<FeedBack> feedBacks;


    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "brand_id")
    Brand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "origin_id")
    Origin origin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "skin_type_id")
    SkinType skinType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tbl_product_has_feature",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    Set<Feature> features;

    @OneToMany(mappedBy = "product")
    List<Step> steps;
}
