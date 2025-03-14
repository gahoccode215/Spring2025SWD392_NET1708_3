package com.swd392.skincare_products_sales_system.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.entity.authentication.Role;
import com.swd392.skincare_products_sales_system.entity.booking.BookingOrder;
import com.swd392.skincare_products_sales_system.entity.booking.ImageSkin;
import com.swd392.skincare_products_sales_system.entity.product.FeedBack;
import com.swd392.skincare_products_sales_system.entity.quiz.Result;
import com.swd392.skincare_products_sales_system.entity.routine.Routine;
import com.swd392.skincare_products_sales_system.enums.Gender;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.entity.*;
import com.swd392.skincare_products_sales_system.entity.authentication.Otp;

import com.swd392.skincare_products_sales_system.entity.booking.ProcessBookingOrder;

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
    Status status;

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
    @JsonIgnore
    List<ImageSkin> imageSkins;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<ProcessBookingOrder> processBookingOrders;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "tbl_user_voucher",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "voucher_id")
    )
    List<Voucher> vouchers;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<Address> addresses;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    List<Routine> routines;


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    List<Otp> otps;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    List<FeedBack> feedBacks;


    public void addOtp(Otp obj) {
        if (this.otps == null) {
            this.otps = new ArrayList<>();
        }
        otps.add(obj);
        obj.setUser(this);
    }

    public void addVoucher(Voucher obj) {
        if (this.vouchers == null) {
            this.vouchers = new ArrayList<>();
        }
        vouchers.add(obj);
        obj.addUser(this);
    }
    public void removeVoucher(Voucher obj){
        for(Voucher voucher : vouchers){
            if(voucher.getCode().equals(obj.getCode())) {
                vouchers.remove(voucher);
                obj.getUsers().remove(this);
                break;
            }
        }
    }
}
