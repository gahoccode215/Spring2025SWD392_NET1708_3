package com.swd392.skincare_products_sales_system.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.entity.routine.Step;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Column(name = "description", columnDefinition = "TEXT", length = 16000)
    String description;

    @Column(name = "ingredient")
    String ingredient;

    @Column(name = "usage_instruction")
    String usageInstruction;

    @Column(name = "thumbnail")
    String thumbnail;

    @Column(name = "rating")
    Double rating;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    Specification specification;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    List<Batch> batches;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonIgnore
    List<Feedback> feedbacks;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "brand_id")
    Brand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "category_id")
    Category category;


    @OneToMany(mappedBy = "product")
    List<Step> steps;

    public void addBatch(Batch obj){
        batches.add(obj);
        obj.setProduct(this);
    }
}
