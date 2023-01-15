package ru.restaurantsvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantsvoting.dto.UserDTO;
import ru.restaurantsvoting.exception.UserAlreadyExistsException;
import ru.restaurantsvoting.mapper.UserMapper;
import ru.restaurantsvoting.model.Role;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/*
TODO
   1. update with current email
   2. toString user and userdto
   3. check all controllers
   4. check security config?
   5. check exceptions and handler
   6. lombok all classes
   7. documents api
   8. tests

 */
@Service
public class UserService {

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserMapper userMapper, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public User save(UserDTO userDTO) {
        String email = userDTO.getEmail();
        userRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserAlreadyExistsException(String.format("User already exists with email: %s", email))
        );
        log.info("Save user: {}", email);
        User user = userMapper.toModel(userDTO);
        user.setRoles(Set.of(Role.USER));
        return userRepository.save(prepareToSave(user));
    }

    @Transactional
    public User update(UserDTO userDTO, int id) {
        User user = findById(id);
        log.info("Update user: from {} to {}", user, userDTO);
        user = userMapper.toModel(userDTO);
        return userRepository.save(prepareToSave(user));
    }

    public void delete(int id) {
        userRepository.delete(findById(id));
    }

    public List<User> findAll() {
        log.info("Get users");
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(String.format("User with id = %d not found", id))
        );
    }

    private User prepareToSave(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
