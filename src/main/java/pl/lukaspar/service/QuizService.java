package pl.lukaspar.service;

import org.springframework.ui.Model;

import java.io.*;
import java.util.List;

public interface QuizService {

    int getUserQuizScore(String quizScoreFilePath) throws IOException;

    void loadQuestion(String questionsFilePath, int numberOfQuestion, Model model) throws IOException;

    int checkUserAnswers(List<String> userAnswers, String quizAnswersPath) throws IOException;

    boolean isBetterScore(int score, String userScoreFilePath) throws IOException;
}
