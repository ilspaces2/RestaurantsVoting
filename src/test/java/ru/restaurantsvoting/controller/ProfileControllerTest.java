package ru.restaurantsvoting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantsvoting.UserDtoTestData.getBadUserDto;
import static ru.restaurantsvoting.UserDtoTestData.getUpdatedUserDto;
import static ru.restaurantsvoting.UserTestData.*;

class ProfileControllerTest extends AbstractControllerTest {

    private final String profileUrl = "/profile";

    @Test
    void delete() throws Exception {
        preform(MockMvcRequestBuilders.delete(profileUrl)
                .header(authorization, getJwtToken(user)))
                .andExpect(status().isNoContent());
        preform(MockMvcRequestBuilders.get(profileUrl, USER_ID)
                .header(authorization, getJwtToken(user)))
                .andExpect(status().isNotFound());
    }

    @Test
    void update() throws Exception {
        preform(MockMvcRequestBuilders.put(profileUrl)
                .header(authorization, getJwtToken(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedUserDto())))
                .andExpect(status().isAccepted())
                .andExpect(content().string(objectMapper.writeValueAsString(getUpdatedUser())));

    }

    @Test
    void whenUpdateWithBadEmailAndPasswordThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.put(profileUrl)
                .header(authorization, getJwtToken(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getBadUserDto())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void get() throws Exception {
        preform(MockMvcRequestBuilders.get(profileUrl)
                .header(authorization, getJwtToken(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(user)));
    }

    @Test
    void whenBadToken() throws Exception {
        preform(MockMvcRequestBuilders.get(profileUrl)
                .header(authorization, "token without roles"))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenIncorrectToken() throws Exception {
        preform(MockMvcRequestBuilders.get(profileUrl)
                .header(authorization, getJwtToken(user) + "incorrect"))
                .andExpect(status().isBadRequest());
    }
}