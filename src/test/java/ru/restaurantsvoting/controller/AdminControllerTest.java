package ru.restaurantsvoting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantsvoting.UserTestData.*;

class AdminControllerTest extends AbstractControllerTest {

    @Test
    @WithMockUser(value = "admin@admin.com", password = "admin", roles = {"ADMIN"})
    void findAll() throws Exception {
        preform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(getUsers())));
    }

    @Test
    @WithMockUser(value = "admin@admin.com", password = "admin", roles = {"ADMIN"})
    void get() throws Exception {
        preform(MockMvcRequestBuilders.get("/admin/{id}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(user)));
    }

    @Test
    @WithMockUser(value = "admin@admin.com", password = "admin", roles = {"ADMIN"})
    void whenGetAndUserNotFoundThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.get("/admin/{id}", BAD_ID))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(value = "admin@admin.com", password = "admin", roles = {"ADMIN"})
    void delete() throws Exception {
        preform(MockMvcRequestBuilders.delete("/admin/{id}", USER_ID))
                .andExpect(status().isNoContent());
        preform(MockMvcRequestBuilders.get("/admin/{id}", USER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "admin@admin.com", password = "admin", roles = {"ADMIN"})
    void whenDeleteAndUserNotFoundThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.delete("/admin/{id}", BAD_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "admin@admin.com", password = "admin", roles = {"ADMIN"})
    void update() throws Exception {
        preform(MockMvcRequestBuilders.put("/admin/{id}", USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedUserDto())))
                .andExpect(status().isNoContent())
                .andExpect(content().string(objectMapper.writeValueAsString(getUpdatedUser())));
    }

    @Test
    @WithMockUser(value = "admin@admin.com", password = "admin", roles = {"ADMIN"})
    void whenUpdateAndUserNotFoundThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.put("/admin/{id}", BAD_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedUserDto())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "admin@admin.com", password = "admin", roles = {"ADMIN"})
    void whenUpdateWithBadEmailAndPasswordThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.put("/admin/{id}", USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getBadUserDto())))
                .andExpect(status().isUnprocessableEntity());
    }
}