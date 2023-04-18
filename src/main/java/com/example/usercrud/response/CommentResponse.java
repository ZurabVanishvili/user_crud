package com.example.usercrud.response;


public class CommentResponse {

    private int id;

    private String commentContent;


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


    @Override
    public String toString() {
        return "CommentResponse{" +
                "id=" + id +
                ", content='" + commentContent + '\'' +
                '}';
    }
}
