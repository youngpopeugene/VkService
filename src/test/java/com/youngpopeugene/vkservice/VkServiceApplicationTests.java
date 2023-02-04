package com.youngpopeugene.vkservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VkServiceApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Value("${vk_service_token}")
    private String vk_service_token;

    @Test
    @WithMockUser(value = "spring")
    public void getUserAndMemberStatusOk() throws Exception {
        String bodyRequest = "{\"user_id\": \"239\", \"group_id\": \"1\"}";

        this.mockMvc.perform(post("/api/v1/vk/getInfo")
                        .header("vk_service_token", vk_service_token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.last_name").value("Степанова"))
                .andExpect(jsonPath("$.first_name").value("Светлана"))
                .andExpect(jsonPath("$.member").value("false"));
    }

    @Test
    @WithMockUser(value = "spring")
    public void getUserAndMemberStatusVkError() throws Exception {
        String bodyRequest = "{\"user_id\": \"239\", \"group_id\": \"239\"}";

        this.mockMvc.perform(post("/api/v1/vk/getInfo")
                        .header("vk_service_token", vk_service_token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("API error: code : 203, message: Access to group denied: access to the group is denied"));

    }

    @Test
    @WithMockUser(value = "spring")
    public void getUserAndMemberStatusVkUserDeleted() throws Exception {
        String bodyRequest = "{\"user_id\": \"3\", \"group_id\": \"239\"}";

        this.mockMvc.perform(post("/api/v1/vk/getInfo")
                        .header("vk_service_token", vk_service_token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Error: user with id = 3 was deleted"));

    }

    @Test
    @WithMockUser(value = "spring")
    public void getUserAndMemberStatusValidationError() throws Exception {
        String bodyRequest = "{\"user_id\": \"-1\", \"group_id\": \"239239239239239\"}";

        this.mockMvc.perform(post("/api/v1/vk/getInfo")
                        .header("vk_service_token", vk_service_token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Validation error: " +
                                "user_id have to be greater than 0," +
                                " group_id have to be integer"));

    }

    @Test
    @WithMockUser(value = "spring")
    public void getUserAndMemberStatusWrongToken() throws Exception {
        String bodyRequest = "{\"user_id\": \"239\", \"group_id\": \"239\"}";

        this.mockMvc.perform(post("/api/v1/vk/getInfo")
                        .header("vk_service_token", "wrong_vk_service_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("API error: code : 5, " +
                                "message: User authorization failed: invalid access_token (4)."));
    }

    @Test
    public void authenticateBadCredentials() throws Exception {
        String bodyRequest = "{\"email\": \"wrong@gmail.com\", \"password\": \"wrong_password\"}";
        this.mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Bad credentials"));
    }


    @Test
    public void registerValidationError() throws Exception {
        String bodyRequest =
                "           {\n" +
                "           \"firstname\": \"eugene\",\n" +
                "           \"lastname\": \"tulyakov\",\n" +
                "           \"email\": \"239\",\n" +
                "           \"password\": \" 1 1 \"\n" +
                "            }";

        this.mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Validation error: " +
                                "invalid format of email, " +
                                "whitespaces are not allowed in the password, " +
                                "password length have to be 8-30 characters"));
    }




}
