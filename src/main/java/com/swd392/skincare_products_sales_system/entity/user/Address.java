package com.swd392.skincare_products_sales_system.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.swd392.skincare_products_sales_system.entity.order.Order;
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
@Table(name = "tbl_address")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "phone")
    String phone;

    @Column(name = "city")
    String city;

    @Column(name = "district")
    String district;

    @Column(name = "ward")
    String ward;

    @Column(name = "street")
    String street;

    @Column(name = "address_line")
    String addressLine;

    @Column(name = "is_default")
    Boolean isDefault;


    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Order> orders;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    User user;
}
