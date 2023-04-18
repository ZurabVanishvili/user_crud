package com.example.usercrud.model;
import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity

public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String mail;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<UserPosts> posts;



    public Users(){}

    public Users(String firstName, String lastName, String mail) {
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

    public void setMail(String email) {
        this.mail = email;
    }



    public List<UserPosts> getPosts() {
        return posts;
    }

    public void updateUser(Users users){
        this.firstName = ofNullable(users.firstName).orElse(firstName);
        this.lastName = ofNullable(users.lastName).orElse(lastName);
        this.mail = ofNullable(users.mail).orElse(mail);
        this.posts = ofNullable(users.posts).orElse(posts);

    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                ", posts=" + posts +
                '}';
    }
}
