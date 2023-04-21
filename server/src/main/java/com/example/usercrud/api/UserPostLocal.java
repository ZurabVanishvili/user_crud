package com.example.usercrud.api;

import com.example.usercrud.entity.UserPosts;

import java.util.List;

public interface UserPostLocal {

    UserPosts getPostById(int id);

    List<UserPosts> getAllPosts(int start, int pageSize);

    void insertPost(UserPosts userPosts);

    void updatePost(int postId, UserPosts newPost);

    void deleteUserPost(int id);

}
