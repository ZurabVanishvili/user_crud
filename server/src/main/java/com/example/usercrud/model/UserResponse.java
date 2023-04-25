package com.example.usercrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@SuppressWarnings("unused")

public class UserResponse {

  private int id;

  private String firstName;

  private String lastName;

  private String mail;

  private String login;

  @JsonIgnore
  private String password;

  private List<UserPostsResponse> posts;

  private List<CommentResponse> comments;

  public UserResponse(){}

    public UserResponse(int id, String firstName, String lastName, String mail,String login,String password, List<UserPostsResponse> posts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.posts = posts;
        this.login = login;
        this.password = password;

    }

    public UserResponse(int id, String firstName, String lastName,
                        String mail,String login,String password,
                        List<UserPostsResponse> posts,List<CommentResponse> comments) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.posts = posts;
        this.login = login;
        this.password = password;
        this.comments = comments;

    }

    public UserResponse(int id, String firstName, String lastName, String mail) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public List<UserPostsResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<UserPostsResponse> posts) {
        this.posts = posts;
    }

    public void setComments(List<CommentResponse> comments) {
        this.comments = comments;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                ", posts=" + posts +
                '}';
    }
}
