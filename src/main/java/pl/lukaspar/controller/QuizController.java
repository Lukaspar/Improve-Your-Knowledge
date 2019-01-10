package pl.lukaspar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lukaspar.service.QuizService;

@Controller
@RequestMapping("/learning")
public class QuizController {

    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/")
    public String learning() {
        return "learning";
    }

    @GetMapping("/javaQuiz")
    public String javaQuiz(Model model) {
        model.addAttribute("javaBasic1ScorePercent", quizService.getUserQuizScore("javaBasic1") * 10);
        return "javaQuiz";
    }

    @GetMapping("/javaQuiz/javaBasic1")
    public String JavaBasic1(Model model) {
        // method load first question
        quizService.loadQuizByName("JavaBasic1");
        quizService.loadQuestion(1, model);
        return "javaBasic1";
    }

    @PostMapping("/javaQuiz/javaBasic1")
    public String JavaBasic1(@RequestParam(required = false, name = "getUserAnswer") String answer, Model model)  {
        // required = false - z tego powodu, że użytkownik może nie podać odpowiedzi, wtedy dostaje 0 pkt za odpowiedz.

        quizService.addUserAnswer(answer);
        int amountOfAnswers = quizService.getNumberOfAnswers();

        if (amountOfAnswers < 11) {
            quizService.loadQuestion(amountOfAnswers, model);
            return "JavaBasic1";
        } else {

            int obtainedScore = quizService.checkUserAnswers();

            // 1 - better, 0 - same, -1 - worse
            int isBetter = quizService.isBetterScore("javaBasic1", obtainedScore);

            if (isBetter == 1) {
                model.addAttribute("betterScore", "Udało Ci się osiągnąć nowy rekord! Zdobyłeś " + obtainedScore + "/10 punktów!");
            } else if (isBetter == 0) {
                model.addAttribute("sameScore", "Osiągnąłeś taki sam wynik jak poprzedno,Zdobyłeś " + obtainedScore + "/10 punktów!");
            }else {
                model.addAttribute("worseScore", "Nie udało Ci się pobić Twojego rekordu, Zdobyłeś " + obtainedScore + "/10 punktów!");
            }

            quizService.clearQuizData();
            return "javaBasic1";
        }
    }



    @GetMapping("/springQuiz")
    public String springQuiz() {
        return "springQuiz";
    }

    @GetMapping("/englishQuiz")
    public String englishQuiz() {
        return "englishQuiz";
    }
}
