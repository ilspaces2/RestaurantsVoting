package ru.restaurantsvoting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurantsvoting.dto.RestaurantDto;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantsvoting.RestaurantTestData.*;
import static ru.restaurantsvoting.UserTestData.admin;
import static ru.restaurantsvoting.UserTestData.user;

class RestaurantControllerTest extends AbstractControllerTest {

    private final String restaurantUrl = "/restaurants";

    @Test
    void save() throws Exception {
        preform(MockMvcRequestBuilders.post(restaurantUrl)
                .header("Authorization", getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RestaurantDto(getNewRestaurant().getName()))))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(getNewRestaurant())));
    }

    @Test
    void addDish() {
    }

    @Test
    void vote() {
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
    void setTime() {
    }
}