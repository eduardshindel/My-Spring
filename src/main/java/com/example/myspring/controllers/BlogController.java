package com.example.myspring.controllers;

import com.example.myspring.models.Post;
import com.example.myspring.repo.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepo postRepo;

    @GetMapping("/blog")
    public String blog(Model model) {
        Iterable<Post> posts = postRepo.findAll();
        model.addAttribute("posts", posts);
        return "blog";
    }

    @GetMapping("/blog/add")
    public String addPost(Model model) {
        return "add-post";
    }

    @PostMapping("/blog/add")
    public String savePost(@RequestParam String title,
                           @RequestParam String description,
                           @RequestParam String text,
                           Model model) {
        Post post = new Post(title, description, text);
        postRepo.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String readPost(@PathVariable(value = "id") Long id, Model model) {
        if (!postRepo.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> opt = postRepo.findById(id);
        ArrayList<Post> post = new ArrayList<>();
        opt.ifPresent(post::add);
        model.addAttribute("post", post);
        return "read-post";
    }

    @GetMapping("/blog/{id}/edit")
    public String editPost(@PathVariable(value = "id") Long id, Model model) {
        if (!postRepo.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> opt = postRepo.findById(id);
        ArrayList<Post> post = new ArrayList<>();
        opt.ifPresent(post::add);
        model.addAttribute("post", post);
        return "edit-post";
    }

    @PostMapping("/blog/{id}/edit")
    public String updatePost(@PathVariable(value = "id") Long id,
                             @RequestParam String title,
                             @RequestParam String description,
                             @RequestParam String text,
                             Model model) {
        Post post = postRepo.findById(id).orElseThrow();
        post.setTitle(title);
        post.setDescription(description);
        post.setText(text);
        postRepo.save(post);
        return "redirect:/blog/{id}";
    }

    @PostMapping("/blog/{id}/delete")
    public String deletePost(@PathVariable(value = "id") Long id, Model model) {
        Post post = postRepo.findById(id).orElseThrow();
        postRepo.delete(post);
        return "redirect:/blog";
    }
}
