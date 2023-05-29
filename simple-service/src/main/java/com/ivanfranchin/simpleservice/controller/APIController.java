package com.ivanfranchin.simpleservice.controller;

import com.ivanfranchin.simpleservice.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.ivanfranchin.simpleservice.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/api")
public class APIController {

    private final MessageService messageService;

    public APIController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/public")
    public String getPublic() {
        return messageService.getPublic();
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/private")
    public String getPrivate(AbstractAuthenticationToken token) {
        return messageService.getPrivate(token.getName());
    }

    @PostMapping("/callback/token")
    public Map<String, String> callbackToken(@RequestBody MultiValueMap<String, String> queryMap) {
        return queryMap.toSingleValueMap();
    }
}
