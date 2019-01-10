package pl.lukaspar.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "score")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    private String username;
    @Column(name = "quiz_name")
    private String quizName;

    @Column(name = "user_score")
    private Integer userScore;

    public Score() {}

    public Score(String quizName, String username, int newScore) {
        this.username = username;
        this.quizName = quizName;
        this.userScore = newScore;
    }
}
