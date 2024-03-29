package com.example.SpringBootBlog.controller;

import com.example.SpringBootBlog.models.Account;
import com.example.SpringBootBlog.reposetories.AccountReposetory;
import com.example.SpringBootBlog.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountReposetory accountReposetory;
//   Note ("/....") IN ONE PAGE I CAN USE ONE TIME @GetMapping  or @PostMapping
    @GetMapping("/login")
    public String getLogin(Model model) {

        model.addAttribute("account", new Account());
        return "login";
    }


// if i want to show anything must do in postmaping getmapping basically show the jason data to postman
@PostMapping("/login")
public String RedirectFromLogin(@ModelAttribute("blogapp") Account account, Model model, HttpSession session) {
    Optional<Account> optionalAccount = accountService.findByEmail(account.getEmail());
    if (optionalAccount.isPresent() && optionalAccount.get().getPassword().equals(account.getPassword())) {
        // Check if the user is already logged in
        Account loggedInUser = (Account) session.getAttribute("loggedInUser");
        if (loggedInUser != null && loggedInUser.getEmail().equals(account.getEmail())) {
            // User is already logged in, redirect to the home page
            return "home";
        } else {
            // User is logging in for the first time or after a delay, log them in and redirect to the home page
            session.setAttribute("loggedInUser", optionalAccount.get());
            model.addAttribute("message", "Hi " + optionalAccount.get().getFirstname() +
                    ". Now click the Ready to Tour button. We are waiting for your new post.");
            return "login";
        }
    } else if (optionalAccount.isEmpty()) {
        // User is not registered, redirect to login-error
        return "login-error";
    } else {
        // Password or email is incorrect, return to 404
        model.addAttribute("message", "Your Password/Email Not Matched");
        return "404";
    }

}



    // i have to do this i  want to redirect user this html page
    @GetMapping("/login-error")
    public String loginError() {
        return "login-error";
    }
    // i have to do this i  want to redirect user this html page
    @GetMapping("/registration_error")
    public String registrationError() {
        return "registration_error";
    }


}