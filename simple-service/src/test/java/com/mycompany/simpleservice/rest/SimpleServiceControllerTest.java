package com.mycompany.simpleservice.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestPropertySource(
        properties = {
                "okta.oauth2.issuer=https://okta.okta.com/oauth2/default",
                "okta.oauth2.client-id=client-id",
                "okta.oauth2.client-secret=client-secret"
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
                .andExpect(content().string("It is public.\n"));
    }

    @Test
    void testGetPrivateStringWithoutToken() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(API_PRIVATE))
                .andDo(print());

        resultActions.andExpect(status().isForbidden());
    }

    @Test
    void testGetPrivateStringWithValidToken() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(API_PRIVATE)
                        .with(oidcLogin()
                                .userInfoToken(builder -> builder.name("User Test").build())))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE_TEXT_PLAIN_UTF8))
                .andExpect(content().string("User Test, it is private.\n"));
    }

    private static final String API_PUBLIC = "/public";
    private static final String API_PRIVATE = "/private";

    private static final MediaType MEDIA_TYPE_TEXT_PLAIN_UTF8 = MediaType.valueOf("text/plain;charset=UTF-8");
}