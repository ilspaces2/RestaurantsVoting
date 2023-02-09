package ru.restaurantsvoting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantsvoting.UserDtoTestData.getBadUserDto;
import static ru.restaurantsvoting.UserDtoTestData.getUpdatedUserDto;
import static ru.restaurantsvoting.UserTestData.*;

class AdminControllerTest extends AbstractControllerTest {

    private final String adminUrl = "/admin";

    private final String adminUrlId = adminUrl + "/{id}";

    private final String roleAdmin = "ADMIN";

    @Test
    @WithMockUser(roles = {roleAdmin})
    void findAll() throws Exception {
        preform(MockMvcRequestBuilders.get(adminUrl))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(getUsers())));
    }

    @Test
    @WithMockUser(roles = {roleAdmin})
    void get() throws Exception {
        preform(MockMvcRequestBuilders.get(adminUrlId, USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(user)));
    }

    @Test
    @WithMockUser(roles = {roleAdmin})
    void whenGetAndUserNotFoundThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.get(adminUrlId, BAD_ID))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(roles = {roleAdmin})
    void delete() throws Exception {
        preform(MockMvcRequestBuilders.delete(adminUrlId, USER_ID))
                .andExpect(status().isNoContent());
        preform(MockMvcRequestBuilders.get(adminUrlId, USER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {roleAdmin})
    void whenDeleteAndUserNotFoundThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.delete(adminUrlId, BAD_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {roleAdmin})
    void update() throws Exception {
        preform(MockMvcRequestBuilders.put(adminUrlId, USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedUserDto())))
                .andExpect(status().isNoContent())
                .andExpect(content().string(objectMapper.writeValueAsString(getUpdatedUser())));
    }

    @Test
    @WithMockUser(roles = {roleAdmin})
    void whenUpdateAndUserNotFoundThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.put(adminUrlId, BAD_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedUserDto())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {roleAdmin})
    void whenUpdateWithBadEmailAndPasswordThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.put(adminUrlId, USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getBadUserDto())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser
    void whenUserNotAdminThenThrowForbiddenException() throws Exception {
        preform(MockMvcRequestBuilders.get(adminUrl))
                .andExpect(status().isForbidden());
    }
}