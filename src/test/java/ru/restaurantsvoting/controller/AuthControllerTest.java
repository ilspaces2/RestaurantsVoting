package ru.restaurantsvoting.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurantsvoting.security.jwt.JwtProvider;
import ru.restaurantsvoting.security.jwt.JwtRequestLogin;
import ru.restaurantsvoting.security.jwt.JwtResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends AbstractControllerTest {

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    void login() throws Exception {
        JwtResponse token = new JwtResponse("token");
        Mockito.when(jwtProvider.generateAccessToken(any())).thenReturn(token.getAccessToken());
        MvcResult action = preform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new JwtRequestLogin("admin@admin.com", "admin"))))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        JwtResponse result = objectMapper.readValue(action.getResponse().getContentAsString(), JwtResponse.class);
        Assertions.assertThat(result).isEqualTo(token);
    }

    @Test
    void whenLoginAndBadCredentialsThenThrowException() throws Exception {
        preform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new JwtRequestLogin("admin", "admin"))))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }
}