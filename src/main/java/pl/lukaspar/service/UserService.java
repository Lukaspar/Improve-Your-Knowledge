package pl.lukaspar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
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

    public User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return findByUsername(auth.getName());
    }


    public int getUserPosition(User user) {

        List<User> listOfUsers = findAll();
        Collections.sort(listOfUsers, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.getScore().compareTo(o1.getScore());
            }
        });

        return listOfUsers.indexOf(user) + 1;
    }

    public boolean deleteByUsername(String password) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = findByUsername(auth.getName());

        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            userRepository.deleteByUsername(user.getUsername());
            return true;
        } else {
            return false;
        }

    }

    public List<User> findAll() {
        return userRepository.findAll();
    }














}
