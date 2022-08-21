package com.ivanfranchin.simpleservice.rest;

import com.ivanfranchin.simpleservice.security.WebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@Import(WebSecurityConfig.class)
@TestPropertySource(properties = {
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://okta.okta.com/oauth2/default"
})
@WebMvcTest(SimpleServiceController.class)
class SimpleServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetPublicString() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(API_PUBLIC))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE_TEXT_PLAIN_UTF8))
                .andExpect(content().string("It is public."));
    }

    @Test
    void testGetPrivateStringWithoutToken() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(API_PRIVATE))
                .andDo(print());

        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    void testGetPrivateStringWithValidToken() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                        get(API_PRIVATE).with(jwt().jwt(jwt -> jwt.claim(JwtClaimNames.SUB, "user@test.com"))))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE_TEXT_PLAIN_UTF8))
                .andExpect(content().string("user@test.com, it is private."));
    }

    private static final String API_PUBLIC = "/public";
    private static final String API_PRIVATE = "/private";

    private static final MediaType MEDIA_TYPE_TEXT_PLAIN_UTF8 = MediaType.valueOf("text/plain;charset=UTF-8");
}