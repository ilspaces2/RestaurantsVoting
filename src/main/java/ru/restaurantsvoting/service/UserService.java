package ru.restaurantsvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.restaurantsvoting.model.Role;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

//    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        log.info("Save user: {}", user.getEmail());
        user.setRoles(Set.of(Role.USER));
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return userRepository.save(user);
    }

    public List<User> findAll() {
        log.info("Get users");
        return userRepository.findAll();
    }
}
