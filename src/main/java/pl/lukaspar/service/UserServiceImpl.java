package pl.lukaspar.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lukaspar.model.Role;
import pl.lukaspar.model.User;
import pl.lukaspar.dao.RoleRepository;
import pl.lukaspar.dao.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        log.info("Finding user by username... ( {} )" , username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        log.info("Finding all users...");
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {

        log.info("Correct new user data. Starting saving user...");

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Set<Role> userRole = new HashSet<>();
        userRole.add(roleRepository.findByRole("USER"));
        user.setRoles(userRole);

        user.setActive(1);
        user.setScore(0);
        user.setDateOfRegistration(LocalDate.now());

        userRepository.save(user);
        log.info("User saved to database correctly.");
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Finding user who is currently logged.");
        return findByUsername(auth.getName());
    }

    @Override
    public int getUserPosition(User user) {

        log.info("Finding user position in rank.");
        List<User> listOfUsers = findAll();

        listOfUsers.sort(((o1, o2) -> o2.getScore().compareTo(o1.getScore())));

        return listOfUsers.indexOf(user) + 1;
    }

    @Override
    public boolean deleteByUsername(String password) {

        User user = getCurrentUser();

        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            userRepository.deleteByUsername(user.getUsername());
            log.info("User ( {} ) deleted correctly.", user.getUsername());
            return true;
        } else {
            log.info("User NOT deleted correctly. Wrong confirm password.");
            return false;
        }

    }

}
