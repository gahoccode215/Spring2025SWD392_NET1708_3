package com.swd392.skincare_products_sales_system.dto.request;

import com.swd392.skincare_products_sales_system.enums.Gender;
import com.swd392.skincare_products_sales_system.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserCreationRequest {
    @NotBlank(message = "firstName must be not blank")
    private String firstName;

    @NotBlank(message = "firstName must be not blank")
    private String lastName;
    private Gender gender;
    private Date birthday;
    private String username;

    @Email(message = "Email invalid")
    private String email;
    private String phone;
    private UserType type;
//    private List<AddressRequest> addresses; // home,office
}
