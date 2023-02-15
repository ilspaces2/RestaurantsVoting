package ru.restaurantsvoting.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurantsvoting.security.jwt.JwtRequestLogin;
import ru.restaurantsvoting.security.jwt.JwtResponse;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantsvoting.UserTestData.user;

class AuthControllerTest extends AbstractControllerTest {

    private final String loginUrl = "/login";

    @Test
    void login() throws Exception {
        MvcResult action = preform(MockMvcRequestBuilders.post(loginUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new JwtRequestLogin(user.getEmail(), user.getPassword()))))
                .andExpect(status().isOk()).andReturn();
        JwtResponse result = objectMapper.readValue(action.getResponse().getContentAsString(), JwtResponse.class);
        Assertions.assertThat(result.getType() + result.getAccessToken()).isEqualTo(getJwtToken(user));
    }

    @Test
    void whenLoginWithBadEmailThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.post(loginUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new JwtRequestLogin("admin", "admin"))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void whenUserNotFoundThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.post(loginUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new JwtRequestLogin("admin@user.com", "adminUser"))))
                .andExpect(status().isUnprocessableEntity());
    }
}