package com.mycompany.simpleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.TypeHint;

@TypeHint(typeNames = "com.okta.spring.boot.oauth.OktaOpaqueTokenIntrospectConditional$IssuerCondition")
@SpringBootApplication
public class SimpleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleServiceApplication.class, args);
    }

}
