package ee.taltech.iti03022024backend.service.impl;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.entity._enum.Role;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.UserRepository;
import ee.taltech.iti03022024backend.service.UserService;
import ee.taltech.iti03022024backend.web.dto.UserDto;
import ee.taltech.iti03022024backend.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User createUser(User user) {
        user.setRoles(Set.of(Role.USER_ROLE));
        return userRepository.save(user);
    }

    @Override
    public UserDto register(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }
        User userEntity = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(userEntity);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto login(String username, String password) {
        User userEntity = userRepository.findByUsername(username);
        if (userEntity != null && userEntity.getPassword().equals(password)) {
            return userMapper.toDto(userEntity);
        }
        throw new RuntimeException("Invalid credentials");
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
//                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findWithProductsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Double getCommonRatingForUser(Long userId) {
        return userRepository.getUserRatingByUserId(userId);
    }
}
