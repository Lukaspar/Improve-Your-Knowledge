package pl.lukaspar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lukaspar.domain.User;
import pl.lukaspar.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    public String registeredUsers(Model model) {

        userService.showLoggedUser(model);
        model.addAttribute("RegisteredUsersFromBase", userService.findAll());
        return "registeredUsers";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/userProfile")
    public String userProfile(Model model) throws IOException {
        userService.showLoggedUser(model);
        userService.showDataAboutUser(model);

        return "userProfile";
    }

    @PostMapping("/userProfile")
    public String userProfile(@RequestParam("password") String password, Model model) throws IOException {
        if(userService.deleteByUsername(password, model)){
            model.addAttribute("succesDelete", "*Pomyślnie usunięto konto użytkownika. Kliknij aby przejść na stronę główną.");
        } else {
            userService.showLoggedUser(model);
            userService.showDataAboutUser(model);
            model.addAttribute("wrongPassword", "*Nie udało się usunąć konta. Hasło nieprawidłowe.");
        }

        return "userProfile";
    }

}
