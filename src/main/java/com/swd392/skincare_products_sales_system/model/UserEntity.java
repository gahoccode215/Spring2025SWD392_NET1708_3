package com.swd392.skincare_products_sales_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name = "tbl_user")
@Slf4j(topic = "UserEntity")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends AbstractEntity<String> implements UserDetails {
    @Column(name = "first_name", length = 255)
    String firstName;

    @Column(name = "last_name", length = 255)
    String lastName;

    @Column(name = "email", length = 255)
    String email;

    @Column(name = "phone", length = 15)
    String phone;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    Date birthday;

    @Column(name = "username", unique = true, nullable = false, length = 255)
    String username;

    @Column(name = "password", length = 255)
    String password;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
