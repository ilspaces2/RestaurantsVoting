package ru.restaurantsvoting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurantsvoting.dto.UserDto;
import ru.restaurantsvoting.model.Role;
import ru.restaurantsvoting.model.User;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminControllerTest extends AbstractControllerTest {

    @Test
    @WithMockUser(value = "admin@admin.com", password = "admin", roles = {"ADMIN"})
    void findAll() throws Exception {
        preform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(getUsersList())));
    }

    @Test
    @WithMockUser(value = "admin@admin.com", password = "admin", roles = {"ADMIN"})
    void get() throws Exception {
        preform(MockMvcRequestBuilders.get("/admin/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(getUsersList().get(0))));
    }

    @Test
    @WithMockUser(value = "admin@admin.com", password = "admin", roles = {"ADMIN"})
    void delete() throws Exception {
        preform(MockMvcRequestBuilders.delete("/admin/{id}", 2))
                .andExpect(status().isNoContent());
        preform(MockMvcRequestBuilders.get("/admin/{id}", 2))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "admin@admin.com", password = "admin", roles = {"ADMIN"})
    void update() throws Exception {
        UserDto userDto = new UserDto("user@user.com", "user1");
        userDto.setName("new name");
        User updated = getUsersList().get(1);
        updated.setName(userDto.getName());
        preform(MockMvcRequestBuilders.put("/admin/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNoContent())
                .andExpect(content().string(objectMapper.writeValueAsString(updated)));
    }

    private List<User> getUsersList() {
        User admin = new User();
        admin.setId(1);
        admin.setName("admin");
        admin.setEmail("admin@admin.com");
        admin.setVoted(false);
        admin.setRoles(Set.of(Role.ADMIN));
        User user = new User();
        user.setId(2);
        user.setEmail("user@user.com");
        user.setName("user1");
        user.setVoted(false);
        user.setRoles(Set.of(Role.USER));
        return List.of(admin, user);
    }
}