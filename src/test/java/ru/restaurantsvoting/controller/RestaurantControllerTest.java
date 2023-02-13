package ru.restaurantsvoting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurantsvoting.dto.RestaurantDto;
import ru.restaurantsvoting.model.Dish;
import ru.restaurantsvoting.model.Restaurant;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.service.RestaurantService;
import ru.restaurantsvoting.service.UserService;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantsvoting.RestaurantTestData.*;
import static ru.restaurantsvoting.UserTestData.*;

class RestaurantControllerTest extends AbstractControllerTest {

    private final String restaurantUrl = "/restaurants";
    private final String addDishes = restaurantUrl + "/dishes/{restaurantName}";
    private final String voteForRestaurant = restaurantUrl + "/vote/{restaurantName}";

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @Test
    void saveRestaurant() throws Exception {
        preform(MockMvcRequestBuilders.post(restaurantUrl)
                .header(authorization, getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RestaurantDto(getNewRestaurant().getName()))))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(getNewRestaurant())));
    }

    @Test
    void whenSaveRestaurantNotAdminRoleThenTrowForbidden() throws Exception {
        preform(MockMvcRequestBuilders.post(restaurantUrl)
                .header(authorization, getJwtToken(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RestaurantDto(getNewRestaurant().getName()))))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenSaveRestaurantWithBlankName() throws Exception {
        preform(MockMvcRequestBuilders.post(restaurantUrl)
                .header(authorization, getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RestaurantDto(" "))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void whenSaveRestaurantWithNameLengthMore120() throws Exception {
        preform(MockMvcRequestBuilders.post(restaurantUrl)
                .header(authorization, getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RestaurantDto("a".repeat(130)))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void whenSaveRestaurantWithNameLengthLess2() throws Exception {
        preform(MockMvcRequestBuilders.post(restaurantUrl)
                .header(authorization, getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RestaurantDto("a"))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void whenSaveRestaurantAndNameAlreadyExistsThenThrowExceptionsAlreadyExists() throws Exception {
        preform(MockMvcRequestBuilders.post(restaurantUrl)
                .header(authorization, getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RestaurantDto(getRestaurantOne().getName()))))
                .andExpect(status().isConflict());
    }

    @Test
    void addDishToRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant(
                getRestaurantTwo().getId(),
                getRestaurantTwo().getName(),
                getNewDishes(),
                getRestaurantTwo().getVotes()
        );
        preform(MockMvcRequestBuilders.put(addDishes, restaurant.getName())
                .header(authorization, getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewDishes())))
                .andExpect(status().isAccepted())
                .andExpect(content().string(objectMapper.writeValueAsString(restaurant)));
    }

    @Test
    void whenAddDishToRestaurantNotAdminRoleThenTrowForbidden() throws Exception {
        preform(MockMvcRequestBuilders.put(addDishes, getRestaurantTwo().getName())
                .header(authorization, getJwtToken(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewDishes())))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAddDishToRestaurantAndRestaurantNotFoundThenTrowNoSuchElement() throws Exception {
        preform(MockMvcRequestBuilders.put(addDishes, "Not found name")
                .header(authorization, getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewDishes())))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenAddDishToRestaurantAndDishNotValid() throws Exception {
        preform(MockMvcRequestBuilders.put(addDishes, getRestaurantTwo().getName())
                .header(authorization, getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(new Dish(null, "", 6000)))))
                .andExpect(status().isUnprocessableEntity());
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
        preform(MockMvcRequestBuilders.put(voteForRestaurant, restaurant.getName())
                .header(authorization, getJwtToken(votedUser)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("\"Voted\""));
        assertThat(restaurantService.get(restaurant.getId()).getVotes()).isEqualTo(restaurant.getVotes());
        assertThat(userService.findById(votedUser.getId()).isVoted()).isTrue();
    }

    @Test
    void whenVoteAndRestaurantNotFoundThenTrowNoSuchElement() throws Exception {
        preform(MockMvcRequestBuilders.put(voteForRestaurant, "Not found name")
                .header(authorization, getJwtToken(user)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenVoteAndUserCancelVote() throws Exception {
        Restaurant restaurant = new Restaurant(
                getRestaurantOne().getId(),
                getRestaurantOne().getName(),
                getRestaurantOne().getDishes(),
                getRestaurantOne().getVotes()
        );
        User votedUser = new User(user);
        restaurantService.setTime(LocalTime.now().plusMinutes(5));
        /* do vote first */
        preform(MockMvcRequestBuilders.put(voteForRestaurant, restaurant.getName())
                .header(authorization, getJwtToken(votedUser)));
        /* do vote second, before vote time */
        preform(MockMvcRequestBuilders.put(voteForRestaurant, restaurant.getName())
                .header(authorization, getJwtToken(votedUser)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("\"Canceled vote\""));
        assertThat(restaurantService.get(restaurant.getId()).getVotes()).isEqualTo(restaurant.getVotes());
        assertThat(userService.findById(votedUser.getId()).isVoted()).isFalse();
    }

    @Test
    void whenVoteAndUserAlreadyVoted() throws Exception {
        Restaurant restaurant = new Restaurant(
                getRestaurantOne().getId(),
                getRestaurantOne().getName(),
                getRestaurantOne().getDishes(),
                getRestaurantOne().getVotes() + 1
        );
        User votedUser = new User(user);
        restaurantService.setTime(LocalTime.now().minusMinutes(5));
        /* do vote first*/
        preform(MockMvcRequestBuilders.put(voteForRestaurant, restaurant.getName())
                .header(authorization, getJwtToken(votedUser)));
        /* do vote second, after vote time*/
        preform(MockMvcRequestBuilders.put(voteForRestaurant, restaurant.getName())
                .header(authorization, getJwtToken(votedUser)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("\"Already voted\""));
        assertThat(restaurantService.get(restaurant.getId()).getVotes()).isEqualTo(restaurant.getVotes());
        assertThat(userService.findById(votedUser.getId()).isVoted()).isTrue();
    }

    @Test
    void findAll() throws Exception {
        preform(MockMvcRequestBuilders.get(restaurantUrl)
                .header(authorization, getJwtToken(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(getRestaurants())));
    }

    @Test
    void getRestaurant() throws Exception {
        preform(MockMvcRequestBuilders.get(restaurantUrl + "/{id}", getRestaurantTwo().getId())
                .header(authorization, getJwtToken(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(getRestaurantTwo())));
    }

    @Test
    void whenGetRestaurantAndNotFoundThenTrowNoSuchElement() throws Exception {
        preform(MockMvcRequestBuilders.get(restaurantUrl + "/{id}", BAD_ID)
                .header(authorization, getJwtToken(user)))
                .andExpect(status().isNotFound());
    }

    @Test
    void setTime() throws Exception {
        preform(MockMvcRequestBuilders.put(restaurantUrl + "/time")
                .header(authorization, getJwtToken(admin))
                .param("time", "14:45"))
                .andExpect(status().isAccepted());
        assertThat(restaurantService.getTime()).isEqualTo("14:45");
    }
}