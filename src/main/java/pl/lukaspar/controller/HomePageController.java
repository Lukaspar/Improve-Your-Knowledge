package pl.lukaspar.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomePageController {

    @GetMapping("/")
    public String homePage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!auth.getName().equals("anonymousUser")) log.info("Logged user: ( {} )", auth.getName());
        else log.info("AnonymousUser is looking HomePage.");

        return "index";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

}
