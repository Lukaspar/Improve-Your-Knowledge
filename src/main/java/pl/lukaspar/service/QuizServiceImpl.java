package pl.lukaspar.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lukaspar.model.User;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
@Transactional
@Slf4j
public class QuizServiceImpl implements QuizService {

    private final UserService userService;

    @Autowired
    public QuizServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public int getUserQuizScore(String quizScoreFilePath) throws IOException {
        // Metoda zwraca obecny najlepszy wynik uzytkownika dla danego testu.

        String userName = userService.getCurrentUser().getUsername();

        List<String> dataFromFile = Files.readAllLines(Paths.get(quizScoreFilePath));
        for (String line : dataFromFile) {
            if (line.split("\\s+")[0].equalsIgnoreCase(userName)) {
                log.info("User exists in file, returning user score.");
                return Integer.parseInt(line.split("\\s+")[1]); // user jest w pliku, zwracam jego score
            }
        }

        log.info("Adding user to file with score.");
        // usera nie ma w pliku, wiec go dodaje, i zwracany jest score 0.
        return appendUserToFile(quizScoreFilePath, userName);
    }

    private int appendUserToFile(String quizScoreFilePath, String userName) throws IOException {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(quizScoreFilePath), StandardOpenOption.APPEND)) {
            bufferedWriter.write(userName + " 0");
            bufferedWriter.newLine();
            return 0;
        }
    }

    @Override
    public void loadQuestion(String questionsFilePath, int numberOfQuestion, Model model) throws IOException {

        log.info("Loading question number {}", numberOfQuestion);

        int positionOfQuestionInFile = (numberOfQuestion - 1) * 5; // input 1, 2, 3... value: 0, 5, 10...

        List<String> listOfQuestions = Files.readAllLines(Paths.get(questionsFilePath));

        model.addAttribute("question", listOfQuestions.get(positionOfQuestionInFile));
        model.addAttribute("answerA", listOfQuestions.get(positionOfQuestionInFile + 1));
        model.addAttribute("answerB", listOfQuestions.get(positionOfQuestionInFile + 2));
        model.addAttribute("answerC", listOfQuestions.get(positionOfQuestionInFile + 3));
        model.addAttribute("answerD", listOfQuestions.get(positionOfQuestionInFile + 4));

    }

    @Override
    public int checkUserAnswers(List<String> userAnswers, String quizAnswersPath) throws IOException {

        log.info("Checking user answers...");
        List<String> correctAnswers = Files.readAllLines(Paths.get(quizAnswersPath));
        int score = 0;

        for (int i = 0; i < 10; ++i) {
            if (userAnswers.get(i) != null && userAnswers.get(i).equalsIgnoreCase(correctAnswers.get(i))) {
                score++;
            }
        }
        return score;
    }

    @Override
    public boolean isBetterScore(int newScore, String userScoreFilePath) throws IOException {

        User currentUser = userService.getCurrentUser();
        int oldUserScore = 0;

        List<String> dataFromFile = Files.readAllLines(Paths.get(userScoreFilePath));
        for (String line : dataFromFile) {
            if (line.split("\\s+")[0].equalsIgnoreCase(currentUser.getUsername())) {
                oldUserScore = Integer.parseInt(line.split("\\s+")[1]);
            }
        }

        log.info("User old score: {} , User new score: {}" , oldUserScore, newScore);

        if (newScore > oldUserScore) {

            log.info("User obtained better score.");

            currentUser.addScore(newScore - oldUserScore);

            for (int i = 0; i < dataFromFile.size(); ++i) {
                if (dataFromFile.get(i).split("\\s+")[0].equalsIgnoreCase(currentUser.getUsername())) {
                    dataFromFile.set(i, currentUser.getUsername() + " " + newScore);
                    break;
                }
            }

            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(userScoreFilePath), StandardOpenOption.CREATE)) {

                for (String line : dataFromFile) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            return true;
        } else {
            log.info("Old score was better.");
            return false;
        }
    }
}


