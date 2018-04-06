package pl.lukaspar.mainproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.lukaspar.mainproject.service.UserService;

@Controller
public class RegisteredUsersController {

    @Autowired
    private UserService userService;

    @GetMapping("/registeredUsers")
    public String registeredUsers(Model model){
        model.addAttribute("RegisteredUsersFromBase", userService.findAll());
        return "RegisteredUsers";
    }


}
