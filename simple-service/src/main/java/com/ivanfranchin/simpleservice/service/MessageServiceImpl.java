package com.ivanfranchin.simpleservice.service;

import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public String getPublic() {
        return "It's a public message.";
    }

    @Override
    public String getPrivate(String name) {
        return "%s, it's a private message.".formatted(name);
    }
}
