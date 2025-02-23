package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.AddressCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.AddressUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.AddressResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.Address;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.repository.AddressRepository;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressServiceImpl implements AddressService {

    AddressRepository addressRepository;
    UserRepository userRepository;

    @Override
    @Transactional
    public void addAddress(AddressCreationRequest request) {
        User user = getAuthenticatedUser();
        if (request.getIsDefault()) {
            // Lấy tất cả các địa chỉ của người dùng
            List<Address> existingAddresses = addressRepository.findByUser(user);
            // Cập nhật tất cả địa chỉ cũ thành không mặc định
            existingAddresses.forEach(address -> address.setIsDefault(false));
            addressRepository.saveAll(existingAddresses); // Lưu tất cả các thay đổi
        }
        Address address = Address.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .city(request.getCity())
                .district(request.getDistrict())
                .ward(request.getWard())
                .street(request.getStreet())
                .addressLine(request.getAddressLine())
                .isDefault(request.getIsDefault())
                .user(user)
                .build();
        addressRepository.save(address);
    }

    @Override
    @Transactional
    public void updateAddress(Long addressId, AddressUpdateRequest request) {
        User user = getAuthenticatedUser();
        Address newaddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        if (request.getIsDefault()) {
            // Lấy tất cả các địa chỉ của người dùng
            List<Address> existingAddresses = addressRepository.findByUser(user);
            // Cập nhật tất cả địa chỉ cũ thành không mặc định
            existingAddresses.forEach(address -> address.setIsDefault(false));
            addressRepository.saveAll(existingAddresses); // Lưu tất cả các thay đổi
        }
        newaddress.setName(request.getName());
        newaddress.setPhone(request.getPhone());
        newaddress.setCity(request.getCity());
        newaddress.setDistrict(request.getDistrict());
        newaddress.setWard(request.getWard());
        newaddress.setStreet(request.getStreet());
        newaddress.setAddressLine(request.getAddressLine());
        newaddress.setIsDefault(request.getIsDefault());
        addressRepository.save(newaddress);
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        addressRepository.delete(address);
    }

    @Override
    public List<AddressResponse> getAllAddresses() {
        User user = getAuthenticatedUser();
        List<Address> addresses = addressRepository.findByUser(user);
        return addresses.stream()
                .map(address -> new AddressResponse(address.getId(),
                        address.getName(), address.getPhone(), address.getCity(),
                        address.getDistrict(), address.getWard(), address.getStreet(),
                        address.getAddressLine(), address.getIsDefault()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressResponse setDefaultAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        // Set all other addresses to non-default
        addressRepository.findByUser(getAuthenticatedUser()).forEach(addr -> addr.setIsDefault(false));
        address.setIsDefault(true);
        addressRepository.save(address);
        return new AddressResponse(address.getId(), address.getName(), address.getPhone(), address.getCity(),
                address.getDistrict(), address.getWard(), address.getStreet(), address.getAddressLine(), true);
    }

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrThrow(username);
    }
}
