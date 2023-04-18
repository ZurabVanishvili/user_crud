package com.example.usercrud.response;

import java.util.List;

@SuppressWarnings("unused")
public class UserPostResponse {


    private int id;

    private String content;

    private String title;


    private List<CommentResponse> comments;


    public UserPostResponse(){}

    public UserPostResponse(String content, String title) {
        this.content = content;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<CommentResponse> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponse> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "UserPosts{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' ;
    }
}
