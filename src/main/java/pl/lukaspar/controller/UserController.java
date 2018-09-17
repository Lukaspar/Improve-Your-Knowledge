package pl.lukaspar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lukaspar.domain.User;
import pl.lukaspar.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    // Constructor Injection
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registration(@Valid User newUser, BindingResult bindingResult, Model model) {

        // Sprawdzam czy użytkownik o danej nazwie juz istnieje
        if ((userService.findByUsername(newUser.getUsername()) != null)) {
            model.addAttribute("existedUserError", "*Ups, Użytkownik o takiej nazwie już istnieje.");
            return "register";
        }

        // Sprawdzam czy hasła są identyczne
        if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
            model.addAttribute("wrongConfirmPassword", "*Ups, hasła w obu polach nie są identyczne.");
            return "register";
        }

        // Sprawdzam czy wystapiły błędy zdefinowanie w encji User
        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            userService.save(newUser);
            model.addAttribute("successRegistration", "*Rejestracja przebiegła pomyślnie. Zaloguj się.");

            return "register";
        }
    }

    @GetMapping("/login")
    public String login() {
        /*
            Proces logowania obsługiwany jest przez Spring Security, ten kontroler
            jedynie wyświetla stronę z logowaniem.
        */
        return "login";
    }

    @GetMapping("/userProfile")
    public String userProfile(Model model) {
        User currentUser = userService.getCurrentUser();

        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("dateOfRegister", currentUser.getDateOfRegistration());
        model.addAttribute("score", currentUser.getScore());
        model.addAttribute("userPosition", userService.getUserPosition(currentUser));

        return "userProfile";
    }

    @PostMapping("/userProfile")
    public String userProfile(@RequestParam("password") String password, Model model) {

        boolean isDeletedUser = userService.deleteByUsername(password);

        if (isDeletedUser) {
            model.addAttribute("successDelete", "*Pomyślnie usunięto konto użytkownika. Kliknij aby przejść na stronę główną.");
            SecurityContextHolder.clearContext(); // Po usunięciu użytkownika czyszczę sesje.
            return "userProfile";
        } else {
            model.addAttribute("wrongPassword", "*Nie udało się usunąć konta. Hasło nieprawidłowe.");
            return userProfile(model);
        }
    }

    @GetMapping("/registeredUsers")
    public String registeredUsers(Model model) {

        model.addAttribute("registeredUsersFromBase", userService.findAll());
        return "registeredUsers";
    }

    @GetMapping("/scoreboard")
    public String usersScoreboard(Model model) {

        List<User> listOfUsers = userService.findAll();

        /*
            Sortuję użytkowników według liczby punktów oraz ucinam tylko do
            pierwszych 10.
         */
        listOfUsers.sort((o1, o2) -> o2.getScore().compareTo(o1.getScore()));

        if (listOfUsers.size() > 10) {
            listOfUsers = listOfUsers.subList(0, 10);
        }

        model.addAttribute("RegisteredUsersFromBase", listOfUsers);

        return "scoreboard";
    }

}
