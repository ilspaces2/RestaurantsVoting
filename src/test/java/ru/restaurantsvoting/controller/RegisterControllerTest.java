package ru.restaurantsvoting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurantsvoting.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantsvoting.UserTestData.*;

class RegisterControllerTest extends AbstractControllerTest {

    private final String registerUrl = "/register";

    @Test
    void register() throws Exception {
        MvcResult action = preform(MockMvcRequestBuilders.post(registerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewUserDto())))
                .andExpect(status().isCreated()).andReturn();
        User actualResult = objectMapper.readValue(action.getResponse().getContentAsString(), User.class);
        User expectedResult = getNewUser();
        expectedResult.setId(actualResult.getId());
        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.getEmail()).isEqualTo(expectedResult.getEmail());
        assertThat(actualResult.getName()).isEqualTo(expectedResult.getName());
    }

    @Test
    void whenRegisterAndUserAlreadyExistsThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.post(registerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUserDto())))
                .andExpect(status().isConflict());
    }

    @Test
    void whenRegisterAndBadEmailAndPasswordThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.post(registerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getBadUserDto())))
                .andExpect(status().isUnprocessableEntity());
    }
}