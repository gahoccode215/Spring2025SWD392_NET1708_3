package com.swd392.skincare_products_sales_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.enums.Gender;
import com.swd392.skincare_products_sales_system.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tbl_user")
public class User extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "first_name", length = 255)
    String firstName;

    @Column(name = "last_name", length = 255)
    String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;

    @Column(name = "email", length = 255)
    String email;

    @Column(name = "phone", length = 15)
    String phone;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    LocalDate birthday;

    @Column(name = "avatar")
    String avatar;

    @Column(name = "point")
    Integer point;

    @Column(name = "username", unique = true, nullable = false, length = 255)
    String username;

    @Column(name = "password", length = 255)
    String password;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status = Status.ACTIVE;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Result> results;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<BookingOrder> bookingOrders;

    @OneToMany(mappedBy = "user")
    List<ImageSkin> imageSkins;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tbl_user_voucher",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "voucher_id")
    )
     Set<Voucher> vouchers;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<Address> addresses;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<Routine> routines;

}
