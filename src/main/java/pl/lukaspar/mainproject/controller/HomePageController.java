package pl.lukaspar.mainproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.lukaspar.mainproject.service.UserService;

@Controller
public class HomePageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homePage(Model model){
        userService.showLoggedUser(model);
        return "index";
    }

}
