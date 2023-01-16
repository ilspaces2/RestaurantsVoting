package ru.restaurantsvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    public User save(User user) {
        String email = user.getEmail();
        userRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserAlreadyExistsException(String.format("User already exists with email: %s", email))
        );
        user.setRoles(Set.of(Role.USER));
        log.info("Save user: {}", user);
        return userRepository.save(prepareToSave(user));
    }

    @Transactional
    public User update(UserDTO userDTO, int id) {
        User user = findById(id);
        userDTO.setId(id);
        User rzl = userMapper.toModel(userDTO);
        rzl.setRoles(user.getRoles());
        log.info("Update user: from {} to {}", user, userDTO);
        return userRepository.save(prepareToSave(rzl));
    }

    public void delete(int id) {
        if (findById(id).getRoles().stream().anyMatch(role -> role.equals(Role.ADMIN))) {
            throw new IllegalArgumentException("Can`t delete account");
        }
        userRepository.deleteById(id);
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
