package com.example.usercrud.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "userPosts")
@SuppressWarnings("unused")
public class UserPosts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    @Column(unique = true)
    private String title;

    @JsonIgnore
    private Timestamp creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;



    @PrePersist
    public void prePersist() {
        if (this.creationDate == null) {
            this.creationDate = new Timestamp(System.currentTimeMillis());
        }
    }


    public UserPosts(){}

    public UserPosts(String content, String title, Timestamp creationDate, User owner, List<Comment> comments) {
        this.content = content;
        this.title = title;
        this.creationDate = creationDate;
        this.owner = owner;
        this.comments = comments;
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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void updateUserPosts(UserPosts userPosts){
        this.content = ofNullable(userPosts.content).orElse(content);
        this.title = ofNullable(userPosts.title).orElse(title);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPosts(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPosts(null);
    }

    public List<Comment> getComments() {
        return comments;
    }


    @Override
    public String toString() {
        return "UserPosts{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", comments=" + comments +
                '}';
    }
}
