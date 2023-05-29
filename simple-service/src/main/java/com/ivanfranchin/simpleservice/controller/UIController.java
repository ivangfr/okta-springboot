package com.ivanfranchin.simpleservice.controller;

import com.ivanfranchin.simpleservice.service.MessageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    private final MessageService messageService;

    public UIController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal OidcUser user) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("avatar", DICE_BEAR_URL_TEMPLATE.formatted(System.currentTimeMillis()));
        model.addAttribute("email", user.getEmail());
        model.addAttribute("publicMessage", messageService.getPublic());
        model.addAttribute("privateMessage", messageService.getPrivate(user.getName()));
        return "index";
    }

    private static final String DICE_BEAR_URL_TEMPLATE = "https://api.dicebear.com/6.x/avataaars/svg?seed=%s";
}
