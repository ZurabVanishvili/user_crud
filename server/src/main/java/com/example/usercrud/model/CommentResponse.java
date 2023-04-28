package com.example.usercrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("unused")
public class CommentResponse {

    private int id;

    private String commentContent;

    private int post_id;

    @JsonIgnore
    private UserPostsResponse posts;

    public CommentResponse(int id, String commentContent, int post_id) {
        this.id = id;
        this.commentContent = commentContent;
        this.post_id = post_id;
    }

    public CommentResponse(int id, String commentContent, UserPostsResponse posts) {
        this.id = id;
        this.commentContent = commentContent;
        this.posts = posts;
    }

    public CommentResponse(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return commentContent;
    }

    public void setContent(String content) {
        this.commentContent = content;
    }

    public int getPost_id() {
        return post_id;
    }

    public UserPostsResponse getPosts() {
        return posts;
    }

    public void setPosts(UserPostsResponse posts) {
        this.posts = posts;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    @Override
    public String toString() {
        return "CommentResponse{" +
                "id=" + id +
                ", content='" + commentContent + '\'' +
                '}';
    }
}
