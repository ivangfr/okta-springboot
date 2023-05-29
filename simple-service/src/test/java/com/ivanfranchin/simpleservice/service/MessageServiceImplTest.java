package com.ivanfranchin.simpleservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(MessageServiceImpl.class)
class MessageServiceImplTest {
    
    @Autowired
    private MessageService messageService;

    @Test
    void testGetPublic() {
        assertThat(messageService.getPublic()).isEqualTo("It's a public message.");
    }

    @Test
    void testGetPrivate() {
        assertThat(messageService.getPrivate("username")).isEqualTo("username, it's a private message.");
    }
}