package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    private JdbcUserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Видалено метод register(Model model)

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user) {
        if (userDetailsManager.userExists(user.getUsername())) {
            return "redirect:/register?error";
        }

        userDetailsManager.createUser(new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            passwordEncoder.encode(user.getPassword()),
            AuthorityUtils.createAuthorityList("USER")
        ));

        return "redirect:/login";
    }
}