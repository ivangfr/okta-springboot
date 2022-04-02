package com.mycompany.simpleservice.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

import static com.mycompany.simpleservice.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
public class SimpleServiceController {

    @Operation(summary = "Get string from public endpoint")
    @GetMapping("/public")
    public String getPublicString() {
        return "It is public.\n";
    }

    @Operation(
            summary = "Get string from private/secured endpoint",
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/private")
    public String getPrivateString(Principal principal) {
        return String.format("%s, it is private.%n", principal.getName());
    }

    @Operation(summary = "Endpoint used by Okta to send back the JWT access token")
    @PostMapping("/callback/token")
    public Map<String, String> callbackToken(@RequestBody MultiValueMap<String, String> queryMap) {
        return queryMap.toSingleValueMap();
    }
}
