package com.swd392.skincare_products_sales_system.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@ToString
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_brand")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Brand extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "slug", unique = true)
    String slug;

    @Column(name = "description", length = 20000)
    String description;

    @Column(name = "thumbnail")
    String thumbnail;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    Set<Product> products;
}
