package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.constant.PredefinedRole;
import com.swd392.skincare_products_sales_system.constant.Query;
import com.swd392.skincare_products_sales_system.dto.request.*;
import com.swd392.skincare_products_sales_system.dto.response.ProductPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.UserPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.UserResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.mapper.UserMapper;
import com.swd392.skincare_products_sales_system.model.Category;
import com.swd392.skincare_products_sales_system.model.Product;
import com.swd392.skincare_products_sales_system.model.Role;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.repository.RoleRepository;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public UserPageResponse getUsers(boolean admin, String keyword, int page, int size, String roleName, Status status, String sortBy, String order) {
        if (page > 0) page -= 1; // Hỗ trợ trang bắt đầu từ 0 hoặc 1
        Sort sort = getSort(sortBy, order);
        Pageable pageable = PageRequest.of(page, size, sort);
        Role role = roleName != null ? roleRepository.findByName(roleName).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)) : null;
        Page<User> users = null;
        if (admin)
            users = userRepository.findAllByFilters(keyword, status, role, pageable);
        log.info("{}", users);
        // Chuyển đổi từ `Page<Product>` sang `ProductPageResponse`
        UserPageResponse response = new UserPageResponse();
        if (users == null) throw new AppException(ErrorCode.USER_NOT_EXISTED);
        response.setUserResponses(users.stream().map(userMapper::toUserResponse).collect(Collectors.toList()));
        response.setTotalElements(users.getTotalElements());
        response.setTotalPages(users.getTotalPages());
        response.setPageNumber(users.getNumber());
        response.setPageSize(users.getSize());
        return response;
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        log.info(request.getRoleName());
        Role role = roleRepository.findByName(request.getRoleName()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .birthday(request.getBirthday())
                .username(request.getUsername())
                .status(Status.ACTIVE)
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(role)
                .build();
        user.setIsDeleted(false);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getUser(String userId) {
        return userMapper.toUserResponse(userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }


    @Override
    @Transactional
    public UserResponse updateUser(UserUpdateRequest request, String userId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getBirthday() != null) {
            user.setBirthday(request.getBirthday());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getRoleName() != null) {
            Role role = roleRepository.findByName(request.getRoleName()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            user.setRole(role);
        }
        return userMapper.toUserResponse(user);
    }


    @Override
    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changeUserStatus(String userId, Status status) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userRepository.updateUserStatus(user.getId(), status);
    }

    @Override
    public UserResponse getUserProfile() {
        String username = null;
        try {
            var context = SecurityContextHolder.getContext();
            username = context.getAuthentication().getName();
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Tìm người dùng theo username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        // Chuyển đổi từ đối tượng User sang UserResponse (DTO)
//        return userMapper.toUserResponse(user);
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    private Sort getSort(String sortBy, String order) {
        // Kiểm tra xem sortBy có phải là null, nếu có thì sắp xếp theo firstName mặc định
        if (sortBy == null) {
            sortBy = Query.FIRST_NAME; // Mặc định là sắp xếp theo firstName nếu không có sortBy
        }

        // Kiểm tra giá trị của order (ASC hoặc DESC)
        if (order == null || (!order.equals(Query.ASC) && !order.equals(Query.DESC))) {
            order = Query.ASC; // Mặc định là theo chiều tăng dần nếu không có order hoặc order không hợp lệ
        }

        // Kiểm tra giá trị sortBy và tạo Sort tương ứng
        switch (sortBy) {
            case Query.FIRST_NAME:
                return order.equals(Query.ASC) ? Sort.by(Query.FIRST_NAME).ascending() : Sort.by(Query.FIRST_NAME).descending();
            case Query.LAST_NAME:
                return order.equals(Query.ASC) ? Sort.by(Query.LAST_NAME).ascending() : Sort.by(Query.LAST_NAME).descending();
            case Query.EMAIL:
                return order.equals(Query.ASC) ? Sort.by(Query.EMAIL).ascending() : Sort.by(Query.EMAIL).descending();
            case Query.USER_NAME:
                return order.equals(Query.ASC) ? Sort.by(Query.USER_NAME).ascending() : Sort.by(Query.USER_NAME).descending();
            default:
                // Nếu không có sortBy hợp lệ, sắp xếp theo firstName mặc định
                return order.equals(Query.ASC) ? Sort.by(Query.FIRST_NAME).ascending() : Sort.by(Query.FIRST_NAME).descending();
        }
    }

}
