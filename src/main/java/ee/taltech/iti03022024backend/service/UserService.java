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

import java.util.Set;

@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserService self;

    @Transactional
    public UserDto createUser(UserDto user) {
        log.info("Attempting to create user with username: {}", user.getUsername());
        try {
            User userEntity = userMapper.toEntity(user);
            userEntity.setRoles(Set.of(Role.ROLE_USER));
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            User createdUser = userRepository.save(userEntity);
            log.info("Successfully created user with id: {}", createdUser.getId());
            return userMapper.toDto(createdUser);
        } catch (Exception e) {
            log.error("Error occurred while creating user with username: {}", user.getUsername(), e);
            throw e;
        }
    }

    @Transactional
    public UserDto updateUser(UserDto user) {
        log.info("Attempting to update user with id: {}", user.getId());
        try {
            User userEntity = userMapper.toEntity(user);
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
        try {
            userRepository.deleteById(id);
            log.info("Successfully deleted user with id: {}", id);
        } catch (Exception e) {
            log.error("Error occurred while deleting user with id: {}", id, e);
            throw e;
        }
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
        } catch (Exception e) {
            log.error("Unexpected error while fetching user by username: {}", username, e);
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
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found in getUserByIdWithProducts: {}", e.getMessage(), e);
            throw e;
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
                        log.warn("User with id {} not found", id);
                        return new ResourceNotFoundException("User with id " + id + " not found");
                    });
            Hibernate.initialize(user.getReviews());
            log.info("Successfully fetched user with id {} and initialized reviews", id);
            return userMapper.toDto(user);
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching user by id: {}", id, e);
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
                        return new ResourceNotFoundException("User with id " + userId + " not found");
                    });
            log.info("Successfully fetched user with id: {}", userId);
            return userMapper.toDto(user);
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching user by id: {}", userId, e);
            throw e;
        }
    }

}
