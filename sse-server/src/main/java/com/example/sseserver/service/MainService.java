package com.example.sseserver.service;

import com.example.sseserver.dto.Post;
import com.example.sseserver.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MainService {

    private final NotificationService notificationService;
    private final PostRepository postRepository;

    @Autowired
    public MainService(
            NotificationService notificationService,
            PostRepository postRepository
    ) {
        this.notificationService = notificationService;
        this.postRepository = postRepository;
    }

    public ArrayList<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public void doLike(int postId) {
        Post foundPost = postRepository.findById(postId);

        notificationService.sendToClient(
                foundPost.getUserId(),
                "'" + foundPost.getTitle() + "' 게시글에 좋아요 획득!",
                "like"
        );
    }


}
