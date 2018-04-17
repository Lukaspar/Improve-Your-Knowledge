package pl.lukaspar.mainproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lukaspar.mainproject.domain.Role;
import pl.lukaspar.mainproject.domain.User;
import pl.lukaspar.mainproject.repository.RoleRepository;
import pl.lukaspar.mainproject.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public void save(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        user.setActive(1);
        user.setDateOfRegistration(LocalDate.now());
        userRepository.save(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }


    public void showLoggedUser(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = findByUsername(auth.getName());

        if(user != null){
            model.addAttribute("loggedUser", "Zalogowany jako: " + user.getUsername());
        }
    }


}
