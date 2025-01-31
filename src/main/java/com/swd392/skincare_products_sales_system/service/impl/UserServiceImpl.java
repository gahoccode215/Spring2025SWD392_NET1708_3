package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.UserCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.UserPasswordRequest;
import com.swd392.skincare_products_sales_system.dto.request.UserUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.UserPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.UserResponse;
import com.swd392.skincare_products_sales_system.enums.UserStatus;
import com.swd392.skincare_products_sales_system.exception.InvalidDataException;
import com.swd392.skincare_products_sales_system.model.UserEntity;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;


    @Override
    public UserPageResponse findAll(String keyword, String sort, int page, int size) {
        return null;
    }

    @Override
    public UserResponse findById(Long id) {
        return null;
    }

    @Override
    public UserResponse findByUsername(String username) {
        return null;
    }

    @Override
    public UserResponse findByEmail(String email) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(UserCreationRequest req) {
        log.info("Saving user: {}", req);

        UserEntity userByEmail = userRepository.findByEmail(req.getEmail());
        if (userByEmail != null) {
            throw new InvalidDataException("Email already exists");
        }

        UserEntity user = new UserEntity();
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setGender(req.getGender());
        user.setBirthday(req.getBirthday());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setUsername(req.getUsername());
//        user.setType(req.getType());
        user.setStatus(UserStatus.NONE);

        UserEntity result = userRepository.save(user);
        log.info("Saved user: {}", user);

//        if (result.getId() != null) {
//            log.info("user id: {}", result.getId());
//            List<AddressEntity> addresses = new ArrayList<>();
//            req.getAddresses().forEach(address -> {
//                AddressEntity addressEntity = new AddressEntity();
//                addressEntity.setApartmentNumber(address.getApartmentNumber());
//                addressEntity.setFloor(address.getFloor());
//                addressEntity.setBuilding(address.getBuilding());
//                addressEntity.setStreetNumber(address.getStreetNumber());
//                addressEntity.setStreet(address.getStreet());
//                addressEntity.setCity(address.getCity());
//                addressEntity.setCountry(address.getCountry());
//                addressEntity.setAddressType(address.getAddressType());
//                addressEntity.setUserId(result.getId());
//                addresses.add(addressEntity);
//            });
//            addressRepository.saveAll(addresses);
//            log.info("Saved addresses: {}", addresses);
//        }
//
//        // Send email verification
//        try {
//            emailService.sendVerificationEmail(req.getEmail(), req.getUsername());
//        } catch (IOException e) {
//            throw new InvalidDataException("Send email failed");
//        }

        return result.getId();
    }

    @Override
    public void update(UserUpdateRequest req) {

    }

    @Override
    public void changePassword(UserPasswordRequest req) {

    }

    @Override
    public void delete(Long id) {

    }
}


