package pl.lukaspar.mainproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukaspar.mainproject.domain.User;
import pl.lukaspar.mainproject.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getRegist(Model model) {
        User newUser = new User();
        model.addAttribute("user", newUser);
        return "register";
    }


    @PostMapping("/register")
    public String postRegist(@Valid User newUser, BindingResult bindingResult, Model model) {

        List<User> usersFromBase = userService.findAll();

        for (User SingleUserFromBase : usersFromBase) {
            if (newUser.getUsername().equalsIgnoreCase(SingleUserFromBase.getUsername())) {
                model.addAttribute("existedUserError", "Ups, Username already exist.");
                return "/register";
            }
        }

        if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
            model.addAttribute("wrongConfirmPassword", "Ups, confirm password doesn't match!");
            return "/register";
        }

        if (bindingResult.hasErrors()) {
            return "/register";
        } else {
            userService.save(newUser);
            model.addAttribute("successRegist", "Success! User has been registered successfully.");
        }

        return "register";
    }


}
