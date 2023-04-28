package com.example.usercrud.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.sql.Timestamp;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "comment")
@SuppressWarnings("unused")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String commentContent;

    @JsonIgnore
    private Timestamp commentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    @JsonIgnore
    private UserPosts posts;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @JsonIgnore
    private User author;


    @PrePersist
    private void setCommentTime() {
        if (this.commentTime == null) {
            this.commentTime = new Timestamp(System.currentTimeMillis());
        }
    }

    public Comment() {}

    public Comment(String commentContent, Timestamp commentTime) {
        this.commentContent = commentContent;
        this.commentTime = commentTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Timestamp getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Timestamp commentTime) {
        this.commentTime = commentTime;
    }

    public void setPosts(UserPosts post) {
        if (this.posts == post) {
            return;
        }
        if (this.posts != null) {
            this.posts.removeComment(this);
        }
        this.posts = post;
        if (post != null) {
            post.addComment(this);
        }
    }



    public void removePost() {
        if (this.posts != null) {
            this.posts.removeComment(this);
            this.posts = null;
        }
    }

    public UserPosts getPosts() {
        return posts;
    }

    public void updateComment(Comment comment) {
        this.commentContent = ofNullable(comment.commentContent).orElse(commentContent);
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", commentContent='" + commentContent + '\'' +
                ", commentTime=" + commentTime +
                '}';
    }

}
