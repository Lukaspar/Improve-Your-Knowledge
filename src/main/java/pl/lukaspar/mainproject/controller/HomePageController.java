package pl.lukaspar.mainproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.lukaspar.mainproject.domain.User;
import pl.lukaspar.mainproject.service.UserService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class HomePageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homePage(Model model){
        userService.showLoggedUser(model);
        return "index";
    }

    @GetMapping("/contact")
    public String contact(Model model){
        userService.showLoggedUser(model);
        return "contact";
    }

    @GetMapping("/leaderBoard")
    public String leaderBoard(Model model){
        userService.showLoggedUser(model);

        List<User> listOfUsers = userService.findAll();
        Collections.sort(listOfUsers, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.getScore().compareTo(o1.getScore());
            }
        });

        if(listOfUsers.size() > 10){
            listOfUsers = listOfUsers.subList(0, 10);
        }

        model.addAttribute("RegisteredUsersFromBase", listOfUsers);

        return "leaderBoard";
    }

    @GetMapping("/learning")
    public String learning(Model model){
        userService.showLoggedUser(model);
        return "learning";
    }

}
