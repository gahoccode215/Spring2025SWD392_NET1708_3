package com.swd392.skincare_products_sales_system.config;

import com.swd392.skincare_products_sales_system.constant.PredefinedRole;
import com.swd392.skincare_products_sales_system.model.Role;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.repository.RoleRepository;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;


    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                List<User> listAccount = new ArrayList<>();
                Role customerRole =roleRepository.save(Role.builder()
                        .name(PredefinedRole.CUSTOMER_ROLE)
                        .description("User role")
                        .build());
                Role managerRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.MANAGER_ROLE)
                        .description("Manager role")
                        .build());
                Role staffRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.STAFF)
                        .description("Staff role")
                        .build());
                Role deliveryRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.DELIVERY)
                        .description("Delivery role")
                        .build());
                Role expertRole = roleRepository.save(Role.builder()

                        .name(PredefinedRole.EXPERT)
                        .description("Expert role")
                        .build());
                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());


                User admin = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .role(adminRole)
                        .build();
                listAccount.add(admin);

                User customer = User.builder()
                        .username("customer")
                        .password(passwordEncoder.encode("customer"))
                        .role(customerRole)
                        .build();
                listAccount.add(customer);

                User manager = User.builder()
                        .username("manager")
                        .password(passwordEncoder.encode("manager"))
                        .role(managerRole)
                        .build();
                listAccount.add(manager);
                User staff = User.builder()
                        .username("staff")
                        .password(passwordEncoder.encode("staff"))
                        .role(staffRole)
                        .build();
                listAccount.add(staff);

                User delivery = User.builder()
                        .username("delivery")
                        .password(passwordEncoder.encode("delivery"))
                        .role(deliveryRole)
                        .build();
                listAccount.add(delivery);

                User expert = User.builder()
                        .username("expert")
                        .password(passwordEncoder.encode("expert"))
                        .role(expertRole)
                        .build();
                listAccount.add(expert);
                userRepository.saveAll(listAccount);
                log.warn("admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}
