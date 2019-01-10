package pl.lukaspar.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;

    @Column(name = "quiz_name")
    private String quizName;

    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    @Column(name = "correct_answer")
    private String correctAnswer;

}
