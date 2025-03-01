package com.swd392.skincare_products_sales_system.model;
import com.swd392.skincare_products_sales_system.model.product.Product;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tbl_skin_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkinType extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "type")
    String type;

    @OneToMany(mappedBy = "skinType", fetch = FetchType.EAGER)
    Set<Product> products;
}
