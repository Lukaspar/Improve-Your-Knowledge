package pl.lukaspar.service;

import org.springframework.ui.Model;


public interface QuizService {

    int getUserQuizScore(String quizScoreFilePath);

    int checkUserAnswers();

    int isBetterScore(String quizName, int newScore);

    void loadQuizByName(String quizName);

    void loadQuestion(int numberOfQuestion, Model model);

    void addUserAnswer(String answer);

    int getNumberOfAnswers();

    void clearQuizData();
}
