package com.example.usercrud.response;

import java.util.List;

@SuppressWarnings("unused")

public class UsersResponse {

  private int id;

  private String firstName;

  private String lastName;

  private String mail;

  private List<UserPostsResponse> posts;

  public UsersResponse(){}

    public UsersResponse(int id, String firstName, String lastName, String mail, List<UserPostsResponse> posts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.posts = posts;

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
