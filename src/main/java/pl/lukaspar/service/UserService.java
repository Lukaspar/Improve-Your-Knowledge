package pl.lukaspar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lukaspar.domain.Quest;
import pl.lukaspar.domain.Role;
import pl.lukaspar.domain.User;
import pl.lukaspar.repository.RoleRepository;
import pl.lukaspar.repository.UserRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        user.setActive(1);
        user.setScore(0);
        user.setDateOfRegistration(LocalDate.now());
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }


    public boolean deleteByUsername(String password, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = findByUsername(auth.getName());

        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            userRepository.deleteByUsername(user.getUsername());
            return true;
        } else {
            return false;
        }

    }


    public void showLoggedUser(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = findByUsername(auth.getName());

        if (user != null) {
            model.addAttribute("loggedUser", "Zalogowany jako: " + user.getUsername());
        }


    }

    /*
        Metoda zwraca obecny najlepszy wynik uzytkownika przy kazdym tescie.
        Dodatkowo jesli uzytkownik pierwszy raz odwiedza ta zakladke, dodawany jest
        do pliku txt i przyznawany jest mu wynik 0.
     */
    public int getUserQuizScore(String quizScoreFileName) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = findByUsername(auth.getName()).getUsername();

        String result;
        boolean nameExist = true;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(quizScoreFileName))) {

            String nameFromFile = bufferedReader.readLine();
            while (!userName.equalsIgnoreCase(nameFromFile)) {
                nameFromFile = bufferedReader.readLine();

                if (nameFromFile == null) {
                    nameExist = false;
                    break;
                }
            }

            result = bufferedReader.readLine();
        }

        if (!nameExist) {

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(quizScoreFileName, true))) {
                bufferedWriter.write(userName + "\n");
                bufferedWriter.write("0" + "\n");


                return 0;
            }
        } else return Integer.parseInt(result);

    }


    public void loadQuestion(String questionsFilePath, int numberOfQuestion, Model model) throws IOException {

        // zmniejszam o 1 bo licze od 0 index
        numberOfQuestion -= 1;

        try (BufferedReader bf = new BufferedReader(new FileReader(questionsFilePath))) {

            for(int i = 0 ; i < numberOfQuestion; ++i){
                for(int j = 0 ; j < 5 ; ++j) bf.readLine();
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = findByUsername(auth.getName());

        int currenUserScore;

        try (BufferedReader bf = new BufferedReader(new FileReader(userScoreFilePath))) {

            String oneLine = bf.readLine();
            while (!oneLine.equalsIgnoreCase(user.getUsername())) {
                oneLine = bf.readLine();
            }

            currenUserScore = Integer.parseInt(bf.readLine());

        }

        if (score > currenUserScore) {

            user.addScore(score - currenUserScore);

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

    public void showDataAboutUser(Model model) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = findByUsername(auth.getName());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("dateOfRegister", user.getDateOfRegistration());
        model.addAttribute("score", user.getScore());

        model.addAttribute("userPosition", (getUserPosition(user) + 1));


    }


    public int getUserPosition(User user) {

        List<User> listOfUsers = findAll();
        Collections.sort(listOfUsers, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.getScore().compareTo(o1.getScore());
            }
        });

        return listOfUsers.indexOf(user);
    }


}
