package com.example.purchases;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-entities-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-entities-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LoginTests {

    @Autowired
    private MockMvc _mockMvc;

    @Test
    void correctLoginTest() throws Exception {
        this._mockMvc.perform(formLogin().user("login", "test").password("test"))
                     .andDo(print())
                     .andExpect(status().is3xxRedirection())
                     .andExpect(redirectedUrl("/products"));

        this._mockMvc.perform(post("/logout"));
    }

    @Test
    void badCredentialsTest() throws Exception {
        this._mockMvc.perform(formLogin().user("login", "test1").password("test"))
                     .andDo(print())
                     .andExpect(status().is2xxSuccessful())
                     .andExpect(content().string(containsString("User not exists")));
    }
}
