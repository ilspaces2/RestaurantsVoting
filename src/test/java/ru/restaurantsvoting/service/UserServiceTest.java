package ru.restaurantsvoting.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import static ru.restaurantsvoting.UserDtoTestData.getUpdatedUserDto;
import static ru.restaurantsvoting.UserDtoTestData.getUserDto;
import static ru.restaurantsvoting.UserTestData.*;

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
        when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(user));
        assertThatThrownBy(() -> userService.save(user)).isInstanceOf(AlreadyExistsException.class);
    }

    @Test
    void save() {
        User newUser = getNewUser();
        when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(newUser);
        User actualResult = userService.save(newUser);
        assertThat(actualResult).isEqualTo(newUser);
        assertThat(actualResult.getRoles()).isEqualTo(Set.of(Role.USER));
    }

    @Test
    void whenUpdateAndUserNotFoundByIdThenThrowException() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.update(getUserDto(), BAD_ID)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void update() {
        when(userRepository.findById(any())).thenReturn(Optional.of(getUpdatedUser()));
        when(userMapper.toModel(any())).thenReturn(getUpdatedUser());
        when(userRepository.save(any())).thenReturn(getUpdatedUser());
        User actualResult = userService.update(getUpdatedUserDto(), USER_ID);
        assertThat(actualResult).isEqualTo(getUpdatedUser());
    }

    @Test
    void whenDeleteAndUserNotFoundThenThrowException() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.delete(USER_ID + 1_000)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void whenDeleteAdminThenThrowException() {
        when(userRepository.findById(any())).thenReturn(Optional.of(admin));
        assertThatThrownBy(() -> userService.delete(ADMIN_ID)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void delete() {
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        userService.delete(USER_ID);
        verify(userRepository).findById(USER_ID);
    }

    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(getUsers());
        List<User> actualResult = userService.findAll();
        assertThat(actualResult).isEqualTo(getUsers());
    }

    @Test
    void whenFindByIdAndNotFoundThenThrowException() {
        when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findById(USER_ID)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findById() {
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        User actualResult = userService.findById(USER_ID);
        assertThat(actualResult).isEqualTo(user);
    }
}