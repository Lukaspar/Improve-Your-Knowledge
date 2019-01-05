package pl.lukaspar.service;

import pl.lukaspar.model.User;

import java.util.List;

public interface UserService {

    User findByUsername(String username);

    List<User> findAll();

    void save(User user);

    User getCurrentUser();

    int getUserPosition(User user);

    boolean deleteByUsername(String password);
}
