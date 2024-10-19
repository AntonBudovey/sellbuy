package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.web.dto.UserDto;

public interface UserService {

    User createUser(User user);
    UserDto register(UserDto userDto);
    UserDto login(String username, String password);
    User updateUser(User user);
    void deleteUser(Long id);

    User getUserByUsername(String username);

    User getUserById(Long id);

    Double getCommonRatingForUser(Long userId);
}
