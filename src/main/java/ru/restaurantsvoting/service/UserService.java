package ru.restaurantsvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.restaurantsvoting.dto.UserDTO;
import ru.restaurantsvoting.mapper.UserMapper;
import ru.restaurantsvoting.model.Role;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public User save(UserDTO userDTO) {
        log.info("Save user: {}", userDTO.getEmail());
        User user = userMapper.toModel(userDTO);
        user.setRoles(Set.of(Role.USER));
        user.setEmail(user.getEmail().toLowerCase());
        return userRepository.save(user);
    }

    public List<User> findAll() {
        log.info("Get users");
        return userRepository.findAll();
    }
}
