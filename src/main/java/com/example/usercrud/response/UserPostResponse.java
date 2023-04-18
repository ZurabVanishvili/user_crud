package com.example.usercrud.response;

@SuppressWarnings("unused")
public class UserPostResponse {


    private int id;

    private String content;

    private String title;


//    private List<Comment> comments;




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


    @Override
    public String toString() {
        return "UserPosts{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' ;
    }
}
