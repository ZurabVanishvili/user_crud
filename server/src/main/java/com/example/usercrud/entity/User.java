package com.example.usercrud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "users")

@SuppressWarnings("unused")
public  class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String mail;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserPosts> posts;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;


    public User() {
    }

    public User(String firstName, String lastName, String mail, String login) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.login = login;
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
        if (mail.matches("([a-zA-Z0-9_.-]+)@([a-zA-Z]+)(\\.)([a-zA-Z]+)")) {
            this.mail = mail;
        } else {
            throw new IllegalArgumentException("Invalid email address.");
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (login.matches("^[a-zA-Z0-9._-]{3,20}$")) {
            this.login = login;
        }else{
            throw new IllegalArgumentException("Invalid username");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$")) {
            this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        } else {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain at least one digit," +
                    " one lowercase letter, and one uppercase letter.");
        }
    }


    public void addPost(UserPosts post) {
        if (posts == null) {
            posts = new ArrayList<>();
        }
        post.setOwner(this);
    }

    public void addComment(Comment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comment.setAuthor(this);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<UserPosts> getPosts() {
        return posts;
    }

    public void updateUser(User user) {
        this.firstName = ofNullable(user.firstName).orElse(firstName);
        this.lastName = ofNullable(user.lastName).orElse(lastName);
        this.mail = ofNullable(user.mail).orElse(mail);
        this.posts = ofNullable(user.posts).orElse(posts);
        this.login = ofNullable(user.getLogin()).orElse(login);
        this.password = ofNullable(user.getPassword()).orElse(password);

    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                ", login='" + login + '\'' +
                ", posts=" + posts +
                '}';
    }
}
