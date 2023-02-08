package ru.restaurantsvoting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurantsvoting.dto.UserDto;
import ru.restaurantsvoting.model.Role;
import ru.restaurantsvoting.model.User;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegisterControllerTest extends AbstractControllerTest {

    @Test
    void register() throws Exception {
        UserDto userDto = new UserDto("user123@user123.com", "user123");
        userDto.setName("user123");
        MvcResult action = preform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated()).andReturn();
        User actualResult = objectMapper.readValue(action.getResponse().getContentAsString(), User.class);
        User expectedResult = getUser();
        expectedResult.setId(actualResult.getId());
        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.getEmail()).isEqualTo(expectedResult.getEmail());
        assertThat(actualResult.getName()).isEqualTo(expectedResult.getName());
    }

    @Test
    void whenRegisterAndUserAlreadyExistsThenThrowException() throws Exception {
        UserDto userDto = new UserDto("admin@admin.com", "user123");
        userDto.setName("user123");
        preform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void whenRegisterAndNotValidInputDataThenThrowException() throws Exception {
        UserDto userDto = new UserDto("admin", "user123");
        preform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isUnprocessableEntity());
    }

    private User getUser() {
        User user = new User();
        user.setEmail("user123@user123.com");
        user.setName("user123");
        user.setPassword("user123");
        user.setRoles(Set.of(Role.USER));
        return user;
    }
}