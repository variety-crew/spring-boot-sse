package com.example.sseserver.repository;

import com.example.sseserver.dto.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class PostRepository {

    private final ArrayList<Post> posts;

    public PostRepository() {
        posts = new ArrayList<>();
        posts.add(new Post(1L, 1L, "오늘 점심 머먹지?"));
        posts.add(new Post(2L, 1L, "오늘 저녁 머먹지?????"));
    }

    public ArrayList<Post> findAll() {
        return posts;
    }

    public Post findById(int postId) {
        for (Post post : posts) {
            if (post.getPostId() == postId) {
                return post;
            }
        }

        return null;
    }
}
