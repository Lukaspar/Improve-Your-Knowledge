package pl.lukaspar.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lukaspar.dao.QuizRepository;
import pl.lukaspar.dao.ScoreRepository;
import pl.lukaspar.model.Quiz;
import pl.lukaspar.model.Score;
import pl.lukaspar.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    private final UserService userService;
    private final QuizRepository quizRepository;
    private final ScoreRepository scoreRepository;

    private static List<Quiz> quiz;
    private static List<String> userAnswers;

    @Autowired
    public QuizServiceImpl(UserService userService, QuizRepository quizRepository, ScoreRepository scoreRepository) {
        this.userService = userService;
        this.quizRepository = quizRepository;
        this.scoreRepository = scoreRepository;
        userAnswers = new ArrayList<>();
    }

    @Override
    public int getUserQuizScore(String quizName) {

        String userName = userService.getCurrentUser().getUsername();

        log.info("Getting User: ( {} ), best score for Quiz: ( {} )", userName, quizName);
        Optional<Score> score = Optional.ofNullable(scoreRepository.findByUsernameAndQuizName(userName, quizName));

        if (score.isPresent()) return score.get().getUserScore();
        else return 0;
    }

    @Override
    public int checkUserAnswers() {

        log.info("Checking user answers...");
        int score = 0;

        for (int i = 0; i < 10; ++i) {
            if (userAnswers.get(i) != null && userAnswers.get(i).equalsIgnoreCase(quiz.get(i).getCorrectAnswer())) {
                score++;
            }
        }
        return score;
    }

    @Override
    public int isBetterScore(String quizName, int newScore) {

        User currentUser = userService.getCurrentUser();

        Optional<Score> score = Optional.ofNullable(scoreRepository.findByUsernameAndQuizName(currentUser.getUsername(), quizName));

        if (score.isPresent()) {
            int oldScore = score.get().getUserScore();
            log.info("User old score: {} , User new score: {}", oldScore, newScore);

            if (newScore > oldScore) {
                log.info("User obtained better score.");
                currentUser.addScore(newScore - oldScore);
                userService.updateUserAllScore(currentUser.getUsername(), currentUser.getAllScore());
                scoreRepository.updateScore(currentUser.getUsername(), quizName, newScore);
                return 1; // better
            } else if (newScore == oldScore) {
                log.info("New score is the same as old score.");
                return 0; // same
            } else {
                log.info("Old score was better.");
                return -1; // worse
            }
        } else {
            log.info("It was User first time.");
            if (newScore == 0) {
                log.info("User still has score equal 0.");
                return 0;
            } else {
                scoreRepository.save(new Score(quizName, currentUser.getUsername(), newScore));
                userService.updateUserAllScore(currentUser.getUsername(), newScore);
                return 1;
            }
        }
    }

    @Override
    public void loadQuizByName(String quizName) {
        log.info("Loading quiz: ( {} )", quizName);
        quiz = quizRepository.findAllByQuizName(quizName);
        log.info("Quiz loaded. Size should be 10: ( {} )", quiz.size());
    }

    @Override
    public void loadQuestion(int numberOfQuestion, Model model) {
        log.info("Loading question number {}", numberOfQuestion);

        model.addAttribute("howMuchQuestion", "Pytanie " + numberOfQuestion + " / 10");

        if (numberOfQuestion == 10) model.addAttribute("endOfQuiz", "");
        else model.addAttribute("continueQuiz", "");

        numberOfQuestion--; // index from 0
        Quiz oneRow = quiz.get(numberOfQuestion);

        model.addAttribute("question", oneRow.getQuestion());
        model.addAttribute("answerA", oneRow.getOptionA());
        model.addAttribute("answerB", oneRow.getOptionB());
        model.addAttribute("answerC", oneRow.getOptionC());
        model.addAttribute("answerD", oneRow.getOptionD());
    }

    @Override
    public void addUserAnswer(String answer) {
        userAnswers.add(answer);
    }

    @Override
    public int getNumberOfAnswers() {
        return userAnswers.size() + 1;
    }

    @Override
    public void clearQuizData() {
        quiz.clear();
        userAnswers.clear();
    }
}


