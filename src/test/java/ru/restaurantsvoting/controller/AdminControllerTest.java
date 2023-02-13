package ru.restaurantsvoting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantsvoting.UserDtoTestData.getBadUserDto;
import static ru.restaurantsvoting.UserDtoTestData.getUpdatedUserDto;
import static ru.restaurantsvoting.UserTestData.*;

class AdminControllerTest extends AbstractControllerTest {

    private final String adminUrl = "/admin";

    private final String adminUrlId = adminUrl + "/{id}";

    @Test
    void findAll() throws Exception {
        preform(MockMvcRequestBuilders.get(adminUrl)
                .header(authorization, getJwtToken(admin)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(getUsers())));
    }

    @Test
    void get() throws Exception {
        preform(MockMvcRequestBuilders.get(adminUrlId, USER_ID)
                .header(authorization, getJwtToken(admin)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(user)));
    }

    @Test
    void whenGetAndUserNotFoundThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.get(adminUrlId, BAD_ID)
                .header(authorization, getJwtToken(admin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete() throws Exception {
        preform(MockMvcRequestBuilders.delete(adminUrlId, USER_ID)
                .header(authorization, getJwtToken(admin)))
                .andExpect(status().isNoContent());
        preform(MockMvcRequestBuilders.get(adminUrlId, USER_ID)
                .header(authorization, getJwtToken(admin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteAndUserNotFoundThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.delete(adminUrlId, BAD_ID)
                .header(authorization, getJwtToken(admin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void update() throws Exception {
        preform(MockMvcRequestBuilders.put(adminUrlId, USER_ID)
                .header(authorization, getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedUserDto())))
                .andExpect(status().isNoContent())
                .andExpect(content().string(objectMapper.writeValueAsString(getUpdatedUser())));
    }

    @Test
    void whenUpdateAndUserNotFoundThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.put(adminUrlId, BAD_ID)
                .header(authorization, getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedUserDto())))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdateWithBadEmailAndPasswordThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.put(adminUrlId, USER_ID)
                .header(authorization, getJwtToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getBadUserDto())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void whenUserNotAdminGetOrPutOrDeleteThenThrowForbiddenException() throws Exception {
        String userToken = getJwtToken(user);
        preform(MockMvcRequestBuilders.get(adminUrl)
                .header(authorization, userToken))
                .andExpect(status().isForbidden());
        preform(MockMvcRequestBuilders.put(adminUrl, USER_ID)
                .header(authorization, userToken))
                .andExpect(status().isForbidden());
        preform(MockMvcRequestBuilders.delete(adminUrl, USER_ID)
                .header(authorization, userToken))
                .andExpect(status().isForbidden());
    }
}