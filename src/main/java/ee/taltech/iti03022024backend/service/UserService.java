package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.entity._enum.Role;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.UserRepository;
import ee.taltech.iti03022024backend.web.dto.UserDto;
import ee.taltech.iti03022024backend.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
@Service
public class UserService {

    public static final String NOT_FOUND = " not found";
    public static final String USER_WITH_ID = "User with id ";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserService self;

    @Transactional
    public UserDto createUser(UserDto user) {
        log.info("Attempting to create user with username: {}", user.getUsername());
        User userEntity = userMapper.toEntity(user);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        userEntity.setRoles(roles);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(userEntity);
        log.info("Successfully created user with id: {}", createdUser.getId());
        return userMapper.toDto(createdUser);
    }

    @Transactional
    public UserDto updateUser(UserDto user) {
        log.info("Attempting to update user with id: {}", user.getId());
        try {
            User userEntity = userMapper.toEntity(user);
            User oldUser = userRepository.findWithProductsById(user.getId())
                    .orElseThrow(() -> {
                        log.warn("User with id to update {} not found", user.getId());
                        return new ResourceNotFoundException("User to update with id " + user.getId() + NOT_FOUND);
                    });
            userEntity.setRoles(oldUser.getRoles());
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            User updatedUser = userRepository.save(userEntity);
            log.info("Successfully updated user with id: {}", updatedUser.getId());
            return userMapper.toDto(updatedUser);
        } catch (Exception e) {
            log.error("Error occurred while updating user with id: {}", user.getId(), e);
            throw e;
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Attempting to delete user with id: {}", id);
        if (!userRepository.existsById(id)) {
            log.warn(USER_WITH_ID + id + NOT_FOUND);
            throw new ResourceNotFoundException("User to delete with id " + id + NOT_FOUND);
        }
        userRepository.deleteById(id);
        log.info("Successfully deleted user with id: {}", id);
    }

    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.warn("User with username {} not found", username);
                        return new ResourceNotFoundException("User with username " + username + " not found in getUserByUsername");
                    });
            log.info("Successfully fetched user with username: {}", username);
            return userMapper.toDto(user);
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found in getUserByUsername: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public UserDto getUserByIdWithProducts(Long id) {
        log.info("Fetching user with id {} and initializing products", id);
        try {
            User user = userRepository.findWithProductsById(id)
                    .orElseThrow(() -> {
                        log.warn("User with id {} not found in getUserByIdWithProducts", id);
                        return new ResourceNotFoundException("User with products with id " + id + " not found in getUserByIdWithProducts");
                    });
            Hibernate.initialize(user.getProducts());
            UserDto userDto = userMapper.toDto(user);
            userDto.setCommonRating(self.getCommonRatingForUser(id));
            log.info("Successfully fetched user with id {} and initialized products", id);
            return userDto;
        } catch (Exception e) {
            log.error("Unexpected error while fetching user by id with products: {}", id, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public UserDto getUserByIdWithReviews(Long id) {
        log.info("Fetching user with id {} and initializing reviews", id);
        try {
            User user = userRepository.findWithReviewsById(id)
                    .orElseThrow(() -> {
                        log.warn("User with reviews with id {} not found", id);
                        return new ResourceNotFoundException(USER_WITH_ID + id + NOT_FOUND);
                    });
            Hibernate.initialize(user.getReviews());
            log.info("Successfully fetched user with id {} and initialized reviews", id);
            return userMapper.toDto(user);
        } catch (ResourceNotFoundException e) {
            log.error("Resource" + NOT_FOUND + ": {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Double getCommonRatingForUser(Long userId) {
        log.info("Fetching common rating for user with id: {}", userId);
        try {
            Double rating = userRepository.getUserRatingByUserId(userId);
            log.info("Successfully fetched common rating for user with id {}: {}", userId, rating);
            return rating;
        } catch (Exception e) {
            log.error("Error occurred while fetching common rating for user with id: {}", userId, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        log.info("Fetching user by id: {}", userId);
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        log.warn("User with id {} not found", userId);
                        return new ResourceNotFoundException(USER_WITH_ID + userId + NOT_FOUND);
                    });
            log.info("Successfully fetched user with id: {}", userId);
            return userMapper.toDto(user);
        } catch (Exception e) {
            log.error("Unexpected error while fetching user by id: {}", userId, e);
            throw e;
        }
    }

    public User getUserEntityById(Long userId) {
        log.info("Fetching user by id: {}", userId);
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        log.warn("User with id {} not found", userId);
                        return new ResourceNotFoundException(USER_WITH_ID + userId + NOT_FOUND);
                    });
            log.info("Successfully fetched user with id: {}", userId);
            return user;
        } catch (Exception e) {
            log.error("Unexpected error while fetching user by id: {}", userId, e);
            throw e;
        }
    }
}
