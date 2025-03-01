package com.swd392.skincare_products_sales_system.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Set;

@Entity
@Table(name = "tbl_origin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Origin extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status = Status.ACTIVE;

    @Column(name = "slug", unique = true)
    String slug;

    @Column(name = "description")
    String description;

    @Column(name = "thumbnail")
    String thumbnail;

    @OneToMany(mappedBy = "origin", fetch = FetchType.EAGER)
    @JsonIgnore
    Set<Product> products;
}
