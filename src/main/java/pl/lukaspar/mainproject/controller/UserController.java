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

@Controller
public class UserController {

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

        User existsUser = userService.findByUsername(newUser.getUsername());
        if (existsUser != null) {
            model.addAttribute("existedUserError", "*Ups, Użytkownik o takiej nazwie już istnieje.");
            return "/register";
        }

        if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
            model.addAttribute("wrongConfirmPassword", "*Ups, hasła w obu polach nie są identyczne.");
            return "/register";
        }

        if (bindingResult.hasErrors()) {
            return "/register";
        } else {
            userService.save(newUser);
            model.addAttribute("successRegist", "*Rejestracja przebiegła pomyślnie. Zaloguj się.");
        }

        return "register";
    }

    @GetMapping("/registeredUsers")
    public String registeredUsers(Model model){

        userService.showLoggedUser(model);
        model.addAttribute("RegisteredUsersFromBase", userService.findAll());
        return "registeredUsers";
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }


    @GetMapping("/userProfile")
    public String userProfile(Model model){
        userService.showLoggedUser(model);
        return "userProfile";
    }

}
