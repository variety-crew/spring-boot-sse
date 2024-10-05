package com.example.sseserver.controller;

import com.example.sseserver.dto.Post;
import com.example.sseserver.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/posts")
public class MainController {

    private final MainService mainService;

    @Autowired
    public MainController(
            MainService mainService
    ) {
        this.mainService = mainService;
    }

    @GetMapping("")
    public ResponseEntity<ArrayList<Post>> findAllPosts() {
        ArrayList<Post> foundPosts = mainService.findAllPosts();

        return ResponseEntity.ok().body(foundPosts);
    }

    @PutMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable int postId) {

        mainService.doLike(postId);

        return ResponseEntity.noContent().build();
    }
}
