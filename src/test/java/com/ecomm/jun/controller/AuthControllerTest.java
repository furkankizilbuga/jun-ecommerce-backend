package com.ecomm.jun.controller;

import com.ecomm.jun.dto.UserRequest;
import com.ecomm.jun.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void register() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword("12345678");
        userRequest.setEmail("test@example.com");

        doNothing().when(authenticationService).register(userRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.message").value("Registered successfully!"));
    }

    @Test
    void registerAdmin() throws Exception {
        UserRequest adminRequest = new UserRequest();
        adminRequest.setPassword("12345678");
        adminRequest.setEmail("admin@example.com");

        doNothing().when(authenticationService).registerAdmin(adminRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/register/authority")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(adminRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.message").value("Registered as admin successfully!"));
    }

    public static String jsonToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
