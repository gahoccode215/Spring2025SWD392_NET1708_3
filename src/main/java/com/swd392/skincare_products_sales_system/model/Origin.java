package com.swd392.skincare_products_sales_system.model;

import com.swd392.skincare_products_sales_system.enums.Status;
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
public class Origin extends AbstractEntity{

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

    @OneToMany(mappedBy = "origin", fetch = FetchType.EAGER)
    Set<Product> products;
}
