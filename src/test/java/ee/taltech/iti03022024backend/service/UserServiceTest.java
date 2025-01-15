package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.entity._enum.Role;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.UserRepository;
import ee.taltech.iti03022024backend.web.dto.UserDto;
import ee.taltech.iti03022024backend.web.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserService self;
    private User user;
    private UserDto userDto;
    private User savedUser;
    private UserDto savedUserDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(null, "anton", "Xx7zK@example.com"
                , "$2a$12$4PdxKUC1NZN9cX4WRB4kP.KrxD98xWAmZ12Koy0zTyVLEifnnhFIO"
                , null, null, null);
        userDto = new UserDto(null, "anton", "Xx7zK@example.com", "1", null);
        savedUser = new User(1L, "anton", "Xx7zK@example.com"
                , "$2a$12$4PdxKUC1NZN9cX4WRB4kP.KrxD98xWAmZ12Koy0zTyVLEifnnhFIO"
                , Set.of(Role.ROLE_USER), null, null);
        savedUserDto = new UserDto(1L, "anton", "Xx7zK@example.com", "1", null);
        Mockito.when(passwordEncoder.encode(user.getPassword()))
                .thenReturn("$2a$12$4PdxKUC1NZN9cX4WRB4kP.KrxD98xWAmZ12Koy0zTyVLEifnnhFIO");
    }

    @Test
    void testCreateUserSuccess() {
        Mockito.when(userMapper.toEntity(userDto)).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(savedUser);
        Mockito.when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);
        UserDto createdUser = userService.createUser(userDto);
        assertEquals(savedUserDto, createdUser);
    }

    @Test
    void testUpdateUserSuccess() {
        Mockito.when(userMapper.toEntity(savedUserDto)).thenReturn(savedUser);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);
        Mockito.when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);
        Mockito.when(userRepository.findWithProductsById(1L)).thenReturn(Optional.of(savedUser));
        UserDto updatedUser = userService.updateUser(savedUserDto);
        assertEquals(savedUserDto, updatedUser);

    }

    @Test
    void testUpdateUserThatNotExist() {
        Mockito.when(userMapper.toEntity(savedUserDto)).thenReturn(savedUser);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);
        Mockito.when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);
        Mockito.when(userRepository.findWithProductsById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(savedUserDto));

    }

    @Test
    void testDeleteUserSuccess() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUserThatNotExist() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, times(0)).deleteById(1L);
    }

    @Test
    void testGetUserByUsernameSuccess() {
        Mockito.when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);
        Mockito.when(userRepository.findByUsername("anton")).thenReturn(Optional.of(savedUser));
        UserDto result = userService.getUserByUsername("anton");
        assertEquals(savedUserDto, result);
    }

    @Test
    void testGetUserByUsernameThatNotExist() {
        Mockito.when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);
        Mockito.when(userRepository.findByUsername("anton")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByUsername("anton"));
    }

    @Test
    void testGetUserByIdWithProductsSuccess() {
        Mockito.when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);
        Mockito.when(userRepository.findWithProductsById(1L)).thenReturn(Optional.of(savedUser));
        Mockito.when(self.getCommonRatingForUser(1L)).thenReturn(5.0);
        UserDto result = userService.getUserByIdWithProducts(1L);
        assertEquals(savedUserDto, result);
    }

    @Test
    void testGetUserByIdWithProductsThatNotExist() {
        Mockito.when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);
        Mockito.when(userRepository.findWithProductsById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByIdWithProducts(1L));
    }

    @Test
    void testGetUserByIdWithReviewsSuccess() {
        Mockito.when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);
        Mockito.when(userRepository.findWithReviewsById(1L)).thenReturn(Optional.of(savedUser));
        UserDto result = userService.getUserByIdWithReviews(1L);
        assertEquals(savedUserDto, result);
    }

    @Test
    void testGetUserByIdWithReviewsThatNotExist() {
        Mockito.when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);
        Mockito.when(userRepository.findWithReviewsById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByIdWithReviews(1L));
    }

    @Test
    void testGetCommonRatingForUserSuccess() {
        Mockito.when(userRepository.getUserRatingByUserId(1L)).thenReturn(5.0);
        Double result = userService.getCommonRatingForUser(1L);
        assertEquals(5.0, result);
    }

    @Test
    void testGetUserByIdSuccess() {
        Mockito.when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        UserDto result = userService.getUserById(1L);
        assertEquals(savedUserDto, result);
    }

    @Test
    void testGetUserByIdThatNotExist() {
        Mockito.when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testGetUserEntityByIdSuccess() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        User result = userService.getUserEntityById(1L);
        assertEquals(savedUser, result);
    }

    @Test
    void testGetUserEntityByIdThatNotExist() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserEntityById(1L));
    }
}