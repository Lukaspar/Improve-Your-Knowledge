package pl.lukaspar.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.lukaspar.model.Score;


@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    Score findByUsernameAndQuizName(String username, String quizName);

    @Modifying
    @Query("update Score s set s.userScore = ?3 where s.username = ?1 and s.quizName = ?2")
    void updateScore(String username, String quizName, Integer newScore);

    void deleteByUsername(String username);
}
