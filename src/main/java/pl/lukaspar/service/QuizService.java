package pl.lukaspar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lukaspar.domain.User;

import javax.transaction.Transactional;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class QuizService {

    private final UserService userService;

    @Autowired
    public QuizService(UserService userService) {
        this.userService = userService;
    }


    public int getUserQuizScore(String quizScoreFilePath) throws IOException {

        /*
            Metoda zwraca obecny najlepszy wynik uzytkownika przy kazdym tescie.
            Dodatkowo jesli uzytkownik pierwszy raz odwiedza ta zakladke, dodawany jest
            do pliku txt i przyznawany jest mu wynik 0.
         */

        String userName = userService.getCurrentUser().getUsername();
        String result;
        boolean isNameExist = true;


        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(quizScoreFilePath))) {

            String nameFromFile = bufferedReader.readLine();
            while (!userName.equalsIgnoreCase(nameFromFile)) {
                nameFromFile = bufferedReader.readLine();

                if (nameFromFile == null) {
                    isNameExist = false;
                    break;
                }
            }

            // Jesli znalazlem pasujaca nazwe uzytkownika to czytam nastepna linie - jest to wynik uzytkownika.
            result = bufferedReader.readLine();
        }

        if (!isNameExist) {
            /*
                Uzytkownika nie ma w pliku wiec go do niego zapisuje.
                append: true, oznacza ze nie nadpisuje pliku a dopisuje na koncu.
             */

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(quizScoreFilePath, true))) {
                bufferedWriter.write(userName + "\n");
                bufferedWriter.write("0" + "\n");

                return 0;
            }
        } else return Integer.parseInt(result);

    }

    public void loadQuestion(String questionsFilePath, int numberOfQuestion, Model model) throws IOException {

        // zmniejszam o 1 bo index od 0
        numberOfQuestion -= 1;

        try (BufferedReader bf = new BufferedReader(new FileReader(questionsFilePath))) {

            /*
                W zaleznosci ktore obecnie jest pytanie, przewijam plik do momentu az
                dotre do obecnego pytania.
             */
            for (int i = 0; i < numberOfQuestion; ++i) {
                for (int j = 0; j < 5; ++j) bf.readLine();
            }

            model.addAttribute("question", bf.readLine());
            model.addAttribute("answerA", bf.readLine());
            model.addAttribute("answerB", bf.readLine());
            model.addAttribute("answerC", bf.readLine());
            model.addAttribute("answerD", bf.readLine());

        }
    }

    public int checkUserAnswers(List<String> userAnswers, String quizAnswersPath) throws IOException {
        int score = 0;

        List<String> correctAnswers = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new FileReader(quizAnswersPath))) {

            String answer = bf.readLine();
            while (answer != null) {
                correctAnswers.add(answer);
                answer = bf.readLine();
            }
        }

        for (int i = 0; i < 10; ++i) {
            if (userAnswers.get(i) != null && userAnswers.get(i).equalsIgnoreCase(correctAnswers.get(i))) {
                score++;
            }
        }

        return score;
    }

    public boolean isBetterScore(int score, String userScoreFilePath) throws IOException {

        User user = userService.getCurrentUser();
        int currentUserScore;

        try (BufferedReader bf = new BufferedReader(new FileReader(userScoreFilePath))) {

            String oneLine = bf.readLine();
            while (!oneLine.equalsIgnoreCase(user.getUsername())) {
                oneLine = bf.readLine();
            }

            currentUserScore = Integer.parseInt(bf.readLine());
        }

        if (score > currentUserScore) {

            user.addScore(score - currentUserScore);

            List<String> allDataFromFile = new ArrayList<>();
            int indexOfOldScore = 0;

            try (BufferedReader bf = new BufferedReader(new FileReader(userScoreFilePath))) {
                String temp = bf.readLine();
                int x = 0;

                while (temp != null) {
                    if (temp.equalsIgnoreCase(user.getUsername())) {
                        indexOfOldScore = x + 1;
                    }
                    allDataFromFile.add(temp);
                    temp = bf.readLine();
                    x++;
                }
            }

            allDataFromFile.set(indexOfOldScore, String.valueOf(score));

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(userScoreFilePath))) {

                for (String anAllDataFromFile : allDataFromFile) {
                    bw.write(anAllDataFromFile + "\n");
                }
            }

            return true;
        } else {
            return false;
        }
    }

}
