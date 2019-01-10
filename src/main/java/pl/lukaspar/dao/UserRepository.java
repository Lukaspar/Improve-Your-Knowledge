package pl.lukaspar.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lukaspar.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Modifying
    @Query(value = "delete from User u where u.username = ?1")
    void deleteByUsername(String username);

    @Modifying
    @Query(value = "update User u SET u.allScore = ?1 where u.username = ?2")
    void updateUserAllScore(Integer score, String username);
}
