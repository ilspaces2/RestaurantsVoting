package ru.restaurantsvoting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.security.AuthUser;
import ru.restaurantsvoting.security.jwt.JwtProvider;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
/* @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) change to @Sql,
 * because @DirtiesContext make tests slow (up context every test method).
 */
@Sql(scripts = {"classpath:db/001_ddl_create_tables.sql", "classpath:db/002_dml_add_data.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AbstractControllerTest {

    protected final String authorization = "Authorization";
    @Autowired
    protected JwtProvider jwtProvider;

    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected ResultActions preform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    protected String getJwtToken(User user) {
        return "Bearer " + jwtProvider.generateAccessToken(new AuthUser(user));
    }
}

