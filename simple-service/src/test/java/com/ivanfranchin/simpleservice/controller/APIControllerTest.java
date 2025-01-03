package com.ivanfranchin.simpleservice.controller;

import com.ivanfranchin.simpleservice.security.SecurityConfig;
import com.ivanfranchin.simpleservice.service.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, MessageServiceImpl.class})
@TestPropertySource(properties = {
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://okta.okta.com/oauth2/default",
        "spring.security.oauth2.client.provider.okta.issuer-uri=https://okta.okta.com/oauth2/default"
})
@WebMvcTest(APIController.class)
class APIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetPublic() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(API_PUBLIC))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE_TEXT_PLAIN_UTF8))
                .andExpect(content().string("It's a public message."));
    }

    @Test
    void testGetPrivateWithoutToken() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(API_PRIVATE))
                .andDo(print());

        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    void testGetPrivateWithValidToken() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                        get(API_PRIVATE).with(jwt().jwt(jwt -> jwt.claim(JwtClaimNames.SUB, "user@test.com"))))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE_TEXT_PLAIN_UTF8))
                .andExpect(content().string("user@test.com, it's a private message."));
    }

    private static final String API_PUBLIC = "/api/public";
    private static final String API_PRIVATE = "/api/private";

    private static final MediaType MEDIA_TYPE_TEXT_PLAIN_UTF8 = MediaType.valueOf("text/plain;charset=UTF-8");
}