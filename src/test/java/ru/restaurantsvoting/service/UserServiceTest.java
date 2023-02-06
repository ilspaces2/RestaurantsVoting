package ru.restaurantsvoting.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.restaurantsvoting.dto.UserDto;
import ru.restaurantsvoting.exception.AlreadyExistsException;
import ru.restaurantsvoting.mapper.UserMapper;
import ru.restaurantsvoting.model.Role;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void whenSaveAndUserAlreadyExistsThenThrowException() {
        User user = getUser();
        when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(user));
        assertThatThrownBy(() -> userService.save(user)).isInstanceOf(AlreadyExistsException.class);
    }

    @Test
    void save() {
        User user = getUser();
        when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        User actualResult = userService.save(user);
        assertThat(actualResult).isEqualTo(user);
        assertThat(actualResult.getRoles()).isEqualTo(Set.of(Role.USER));
    }

    @Test
    void whenUpdateAndUserNotFoundByIdThenThrowException() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.update(new UserDto("email", "password"), 11)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void update() {
        User user = getUser();
        UserDto userDto = new UserDto(user.getEmail(), user.getPassword());
        userDto.setName("Ben");
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userMapper.toModel(any())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        User actualResult = userService.update(userDto, user.getId());
        assertThat(actualResult).isEqualTo(user);
    }

    @Test
    void whenDeleteAndUserNotFoundThenThrowException() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.delete(11)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void whenDeleteAdminThenThrowException() {
        User user = getUser();
        user.setRoles(Set.of(Role.ADMIN));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        assertThatThrownBy(() -> userService.delete(11)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void delete() {
        User user = getUser();
        user.setRoles(Set.of(Role.USER));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        userService.delete(user.getId());
        verify(userRepository).findById(user.getId());
    }

    @Test
    void findAll() {
        List<User> users = List.of(getUser());
        when(userRepository.findAll()).thenReturn(users);
        List<User> actualResult = userService.findAll();
        assertThat(actualResult).isEqualTo(users);

    }

    @Test
    void whenFindByIdAndNotFoundThenThrowException() {
        when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findById(getUser().getId())).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findById() {
        User user = getUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        User actualResult = userService.findById(user.getId());
        assertThat(actualResult).isEqualTo(user);
    }

    private User getUser() {
        User user = new User();
        user.setId(123);
        user.setEmail("new@user");
        user.setName("Jack");
        user.setPassword("pass");
        return user;
    }
}