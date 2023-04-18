package com.example.usercrud.response;

import java.util.List;


public class UserResponse {

  private int id;

  private String firstName;

  private String lastName;

  private String mail;

  private List<UserPostResponse> posts;

  public UserResponse(){}


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

    public List<UserPostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<UserPostResponse> posts) {
        this.posts = posts;
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
