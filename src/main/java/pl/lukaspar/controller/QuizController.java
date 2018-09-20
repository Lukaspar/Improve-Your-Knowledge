package pl.lukaspar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lukaspar.service.QuizService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QuizController {

    private static List<String> userAnswer;

    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService){
        this.quizService = quizService;
        userAnswer = new ArrayList<>();
    }

    @GetMapping("/learning")
    public String learning(){
        return "learning";
    }

    @GetMapping("/javaQuiz")
    public String javaQuiz(Model model) throws IOException {

        model.addAttribute("javaBasic1Score", quizService.getUserQuizScore("src/main/resources/txt/java/javaBasic1/javaBasic1UserScore.txt")*10);
        return "javaQuiz";
    }

    @GetMapping("/javaBasic1")
    public String JavaBasic1(Model model) throws IOException {
        model.addAttribute("continueQuiz", "Następny");
        model.addAttribute("howMuchQuestion", "Pytanie 1 / 10");
        quizService.loadQuestion("src/main/resources/txt/java/javaBasic1/javaBasic1Questions.txt", 1, model);
        return "javaBasic1";
    }

    @PostMapping("/javaBasic1")
    public String JavaBasic1(@RequestParam(required = false, name = "getUserAnswer") String answer, Model model) throws IOException {
        /*
             required = false - z tego powodu, że użytkownik może nie podać odpowiedzi, wtedy dostaje 0 pkt za odpowiedz.
        */

        userAnswer.add(answer);

        if(userAnswer.size() < 10){

            model.addAttribute("howMuchQuestion", "Pytanie " + (userAnswer.size()+1) + " / 10");

            if(userAnswer.size() == 9) model.addAttribute("endOfQuiz", "");
            else model.addAttribute("continueQuiz", "");

            quizService.loadQuestion("src/main/resources/txt/java/javaBasic1/javaBasic1Questions.txt", (userAnswer.size()+1), model);
            return "/JavaBasic1";
        } else {

            int score = quizService.checkUserAnswers(userAnswer, "src/main/resources/txt/java/javaBasic1/javaBasic1AnswersKey.txt");
            boolean isBetter = quizService.isBetterScore(score, "src/main/resources/txt/java/javaBasic1/javaBasic1UserScore.txt");

            if(isBetter){
                model.addAttribute("betterScore", "Udało Ci się osiągnąć nowy rekord! Zdobyłeś " + score + "/10 punktów!");
            } else {
                model.addAttribute("worseScore", "Nie udało Ci się pobić Twojego rekordu, zdobyłeś " + score + "/10 punktów!");
            }

            userAnswer.clear();
            return "/javaBasic1";
        }
    }

    @GetMapping("/springQuiz")
    public String springQuiz(){
        return "springQuiz";
    }

    @GetMapping("/englishQuiz")
    public String englishQuiz(){
        return "englishQuiz";
    }
}
