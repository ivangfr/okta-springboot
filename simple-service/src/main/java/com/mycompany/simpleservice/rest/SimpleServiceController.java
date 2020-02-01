package com.mycompany.simpleservice.rest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleServiceController {

    @GetMapping("/public")
    public String getPublicString() {
        return "It is public.\n";
    }

    @GetMapping("/private")
    public String getPrivateString(@AuthenticationPrincipal OidcUser oidcUser) {
        return String.format("%s, it is private.%n", oidcUser.getFullName());
    }

}
