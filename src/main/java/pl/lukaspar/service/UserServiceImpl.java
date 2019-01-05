package pl.lukaspar.service;

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
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Set<Role> userRole = new HashSet<>();
        userRole.add(roleRepository.findByRole("USER"));
        user.setRoles(userRole);

        user.setActive(1);
        user.setScore(0);
        user.setDateOfRegistration(LocalDate.now());

        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return findByUsername(auth.getName());
    }

    @Override
    public int getUserPosition(User user) {

        List<User> listOfUsers = findAll();

        listOfUsers.sort(((o1, o2) -> o2.getScore().compareTo(o1.getScore())));

        return listOfUsers.indexOf(user) + 1;
    }

    @Override
    public boolean deleteByUsername(String password) {

        User user = getCurrentUser();

        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            userRepository.deleteByUsername(user.getUsername());
            return true;
        } else {
            return false;
        }

    }

}
