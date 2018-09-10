package pl.lukaspar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lukaspar.domain.User;
import pl.lukaspar.service.UserService;

import java.io.IOException;
import java.util.*;

@Controller
public class HomePageController {

    private static List<String> userAnswer = new ArrayList<>();

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

    @GetMapping("/javaQuiz")
    public String javaQuiz(Model model) throws IOException {
        userService.showLoggedUser(model);

        model.addAttribute("javaBasic1Score", userService.getUserQuizScore("src/main/resources/txt/javaBasic/javaBasic1UserScore.txt")*10);
        return "javaQuiz";
    }


    @GetMapping("/javaBasic1")
    public String JavaBasic1(Model model) throws IOException {
        userService.showLoggedUser(model);
        model.addAttribute("continueQuiz", "Następny");
        model.addAttribute("howMuchQuestion", "Pytanie 1 / 10");
        userService.loadQuestion("src/main/resources/txt/javaBasic/javaBasic1Questions.txt", 1, model);
        return "javaBasic1";
    }

    @PostMapping("/javaBasic1")
    public String JavaBasic1(@RequestParam(required = false, name = "getUserAnswer") String answer, Model model) throws IOException {
        userService.showLoggedUser(model);
        userAnswer.add(answer);

        if(userAnswer.size() < 10){

            model.addAttribute("howMuchQuestion", "Pytanie " + (userAnswer.size()+1) + " / 10");

            if(userAnswer.size() == 9) model.addAttribute("endOfQuiz", "");
            else model.addAttribute("continueQuiz", "");


            userService.loadQuestion("src/main/resources/txt/javaBasic/javaBasic1Questions.txt", (userAnswer.size()+1), model);
            return "/JavaBasic1";
        } else {
            int score = userService.checkUserAnswers(userAnswer, "src/main/resources/txt/javaBasic/javaBasic1AnswersKey.txt");
            boolean isBetter = userService.isBetterScore(score, "src/main/resources/txt/javaBasic/javaBasic1UserScore.txt");


            if(isBetter){
                model.addAttribute("betterScore", "Udało Ci się osiągnąć nowy rekord! Zdobyłeś " + score + "/10 punktów!");
            } else {
                model.addAttribute("worseScore", "Nie udało Ci się pobić Twojego rekordu, zdobyłeś " + score + "/10 punktów!");
            }

            userAnswer.clear();
            return "/javaBasic1";
        }



    }

}
