package ru.restaurantsvoting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurantsvoting.dto.RestaurantDto;
import ru.restaurantsvoting.model.Restaurant;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.service.RestaurantService;
import ru.restaurantsvoting.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantsvoting.RestaurantTestData.*;
import static ru.restaurantsvoting.UserTestData.admin;
import static ru.restaurantsvoting.UserTestData.user;

class RestaurantControllerTest extends AbstractControllerTest {

    private final String restaurantUrl = "/restaurants";

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @Test
    void saveRestaurant() throws Exception {
        preform(MockMvcRequestBuilders.post(restaurantUrl)
                .header("Authorization", getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RestaurantDto(getNewRestaurant().getName()))))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(getNewRestaurant())));
    }

    @Test
    void addDishToRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant(
                getRestaurantTwo().getId(),
                getRestaurantTwo().getName(),
                getNewDishes(),
                getRestaurantTwo().getVotes()
        );
        preform(MockMvcRequestBuilders.put(restaurantUrl + "/dishes/{restaurantName}", restaurant.getName())
                .header("Authorization", getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewDishes())))
                .andExpect(status().isAccepted())
                .andExpect(content().string(objectMapper.writeValueAsString(restaurant)));
    }

    @Test
    void vote() throws Exception {
        Restaurant restaurant = new Restaurant(
                getRestaurantOne().getId(),
                getRestaurantOne().getName(),
                getRestaurantOne().getDishes(),
                getRestaurantOne().getVotes() + 1
        );
        User votedUser = new User(user);
        preform(MockMvcRequestBuilders.put(restaurantUrl + "/vote/{restaurantName}", restaurant.getName())
                .header("Authorization", getJwtToken(votedUser)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("\"Voted\""));
        assertThat(restaurantService.get(restaurant.getId()).getVotes()).isEqualTo(restaurant.getVotes());
        assertThat(userService.findById(votedUser.getId()).isVoted()).isTrue();
    }

    @Test
    void findAll() throws Exception {
        preform(MockMvcRequestBuilders.get(restaurantUrl)
                .header("Authorization", getJwtToken(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(getRestaurants())));
    }

    @Test
    void get() throws Exception {
        preform(MockMvcRequestBuilders.get(restaurantUrl + "/{id}", getRestaurantOne().getId())
                .header("Authorization", getJwtToken(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(getRestaurantOne())));
    }

    @Test
    void setTime() throws Exception {
        preform(MockMvcRequestBuilders.put(restaurantUrl + "/time")
                .header("Authorization", getJwtToken(admin))
                .param("time", "14:45"))
                .andExpect(status().isAccepted());
        assertThat(restaurantService.getTime()).isEqualTo("14:45");
    }
}