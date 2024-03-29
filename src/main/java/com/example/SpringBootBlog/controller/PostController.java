package com.example.SpringBootBlog.controller;

import com.example.SpringBootBlog.models.Account;
import com.example.SpringBootBlog.models.Post;
import com.example.SpringBootBlog.service.AccountService;
import com.example.SpringBootBlog.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private AccountService accountService;


    @GetMapping("/posts/{id}")
    // i have write posts in getmapping becuse in Seeddata i created the the naame  object name
    // posts if i write anything  @GetMapping("/postsssss/{id}") then is i write in web like this with id 1 or 2 then its  print but after touch anything home page its show weball page errr err
    private String getPost(@PathVariable Long id, Model model) {
        // find the post by id
        Optional<Post> optionalPost = postService.getById(id);
        // if post exists then i have show this into model
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post); // here post becuse post is here object name
            return "post";
        } else {
            return "404";
        }
    }

    // here user can add their post after register
    @GetMapping("/posts/new")
    public String newPost(Model model, HttpSession session) {
        Account loggedInUser = (Account) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "registration_error";
        } else {
            Post post = new Post();
            post.setAccount(loggedInUser);
            model.addAttribute("post", post);
            System.out.println("Redirecting to Pots_new");
            return "Pots_new";
        }
    }
//        Post post = new Post();
//        post.setAccount(loggedInUser); // account set to new user
//        model.addAttribute("post", post);
//        return "Pots_new";


    @PostMapping("/posts/new")
    private String saveNewPost(@ModelAttribute Post post, Model model, HttpSession session) {
        // Check if the user is logged in
        Account loggedInUser = (Account) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "registration_error";
        }

        // Check if the title length is greater than 15
        if (post.getTitle().length() > 15) {
            model.addAttribute("message", "Title must be less than or equal to 15 characters");
            return "404";
        }

        // Set the account for the new post and save it
        post.setAccount(loggedInUser);
        postService.save(post);

        // Get all posts after saving the new post
        List<Post> posts = postService.getAll();
        model.addAttribute("posts", posts);

        return "home"; // Redirect to the home page
    }
}

