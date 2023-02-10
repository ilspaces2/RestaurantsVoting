package ru.restaurantsvoting.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import ru.restaurantsvoting.dto.RestaurantDto;
import ru.restaurantsvoting.exception.AlreadyExistsException;
import ru.restaurantsvoting.mapper.RestaurantMapper;
import ru.restaurantsvoting.model.Dish;
import ru.restaurantsvoting.model.Restaurant;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.repository.DishRepository;
import ru.restaurantsvoting.repository.RestaurantRepository;
import ru.restaurantsvoting.repository.UserRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.restaurantsvoting.RestaurantTestData.*;
import static ru.restaurantsvoting.UserTestData.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private RestaurantMapper restaurantMapper;
    @Mock
    private DishRepository dishRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void whenSaveSuccess() {
        Restaurant restaurant = getNewRestaurant();
        RestaurantDto restaurantDto = new RestaurantDto(restaurant.getName());
        when(restaurantMapper.toModel(any())).thenReturn(restaurant);
        when(restaurantRepository.save(any())).thenReturn(restaurant);
        Restaurant actual = restaurantService.save(restaurantDto);
        assertThat(actual.getName()).isEqualTo(restaurantDto.getName());
        assertThat(actual.getId()).isEqualTo(restaurant.getId());
    }

    @Test
    void whenSaveAndRestaurantAlreadyExistsThenThrowException() {
        Restaurant restaurant = getRestaurantOne();
        RestaurantDto restaurantDto = new RestaurantDto(restaurant.getName());
        when(restaurantRepository.findByName(any())).thenReturn(Optional.of(restaurant));
        assertThatThrownBy(() -> restaurantService.save(restaurantDto)).isInstanceOf(AlreadyExistsException.class);
    }

    @Test
    void findAll() {
        List<Restaurant> restaurants = getRestaurants();
        when(restaurantRepository.findAll()).thenReturn(restaurants);
        List<Restaurant> actualResult = restaurantService.findAll();
        assertThat(actualResult).isEqualTo(restaurants);
    }

    @Test
    void whenVoteAndRestaurantNotFoundByName() {
        when(restaurantRepository.findByName(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> restaurantService.vote(getRestaurantOne().getName(), 0)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void whenVoteAndUserNotFoundById() {
        when(restaurantRepository.findByName(any())).thenReturn(Optional.of(getRestaurantOne()));
        assertThatThrownBy(() -> restaurantService.vote(getRestaurantOne().getName(), BAD_ID)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void whenVoteAndUserAlreadyVoted() {
        Restaurant restaurant = getRestaurantOne();
        User votedUser = new User(user);
        votedUser.setVoted(true);
        when(restaurantRepository.findByName(any())).thenReturn(Optional.of(restaurant));
        when(userRepository.findById(any())).thenReturn(Optional.of(votedUser));
        restaurantService.setTime(LocalTime.now().minusMinutes(1));
        String actualResult = restaurantService.vote(restaurant.getName(), USER_ID);
        assertThat(votedUser.isVoted()).isTrue();
        assertThat(actualResult).isEqualTo("\"Already voted\"");
    }

    @Test
    void whenVoteAndUserCancelVoted() {
        Restaurant restaurant = getRestaurantOne();
        User votedUser = new User(user);
        votedUser.setVoted(true);
        when(restaurantRepository.findByName(any())).thenReturn(Optional.of(restaurant));
        when(userRepository.findById(any())).thenReturn(Optional.of(votedUser));
        restaurantService.setTime(LocalTime.now().plusMinutes(1));
        String actualResult = restaurantService.vote(restaurant.getName(), USER_ID);
        assertThat(votedUser.isVoted()).isFalse();
        assertThat(actualResult).isEqualTo("\"Canceled vote\"");
    }

    @Test
    void voted() {
        Restaurant restaurant = getRestaurantOne();
        User voteUser = new User(user);
        when(restaurantRepository.findByName(any())).thenReturn(Optional.of(restaurant));
        when(userRepository.findById(any())).thenReturn(Optional.of(voteUser));
        String actualResult = restaurantService.vote(restaurant.getName(), USER_ID);
        assertThat(voteUser.isVoted()).isTrue();
        assertThat(actualResult).isEqualTo("\"Voted\"");
    }

    @Test
    void whenAddDishAndRestaurantNotFoundByName() {
        when(restaurantRepository.findByName(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> restaurantService.addDish(getRestaurantOne().getName(), List.of())).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void whenAdd() {
        Dish dish1 = new Dish();
        dish1.setId(2);
        dish1.setPrice(12);
        dish1.setName("Beer");
        List<Dish> dishes = List.of(dish1);
        Restaurant restaurant = getRestaurantTwo();
        when(restaurantRepository.findByName(any())).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);
        Restaurant actualResult = restaurantService.addDish(restaurant.getName(), dishes);
        assertThat(actualResult.getDishes()).isEqualTo(dishes);
    }
}
