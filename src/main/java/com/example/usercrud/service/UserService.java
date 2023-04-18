package com.example.usercrud.service;

import com.example.usercrud.api.CommentLocal;
import com.example.usercrud.api.UserLocal;
import com.example.usercrud.api.UserPostLocal;
import com.example.usercrud.model.Comment;
import com.example.usercrud.model.UserPosts;
import com.example.usercrud.model.Users;
import com.example.usercrud.response.CommentResponse;
import com.example.usercrud.response.UserPostResponse;
import com.example.usercrud.response.UserResponse;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;


@RequestScoped
@Path("users")

public class UserService {

    @EJB
    private UserLocal userLocal;
    @EJB
    private UserPostLocal userPostLocal;

    @EJB
    private CommentLocal commentLocal;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addUser(Users users) {
        userLocal.insertUser(users);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> getAllUsers() {
        return userLocal.getAllUsers();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Users getUser(@PathParam("id") int id) {
        return userLocal.getUserById(id);
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") int id, Users user) {

        userLocal.updateUser(id, user);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") int id) {

        userLocal.deleteUser(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/posts")
    public List<UserPosts> getAllPosts() {
        return userPostLocal.getAllPosts();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/posts/{id}")
    public UserPosts getPostById(@PathParam("id") int id) {
        return userPostLocal.getPostById(id);
    }

    @POST
    @Path("/posts/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addPostToUser(@PathParam(("id")) int id, UserPosts post) {
        Users user = userLocal.getUserById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        userPostLocal.insertPost(post);
        user.addPost(post);
        return Response.status(Response.Status.CREATED).build();
    }

    @PATCH
    @Path("/posts/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updatePostOfUser(@PathParam("id") int id, UserPosts post) {

        userPostLocal.updatePost(id, post);

        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/posts/{id}")
    @Transactional
    public Response deletePost(@PathParam("id") int id) {

        userPostLocal.deleteUserPost(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/comments")
    public Response getAllComments() {
        return Response.status(Response.Status.OK).
                entity(commentLocal.getAllComments()).build();
    }

    @POST
    @Path("/comments/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addComment(@PathParam("id") int id, Comment comment) {

        UserPosts post = userPostLocal.getPostById(id);
        if (post == null) {
            throw new NotFoundException("Post not found");
        }

        commentLocal.addComment(comment);
        post.addComment(comment);

        return Response.status(Response.Status.OK).build();
    }

    @PATCH
    @Path("/comments/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateComment(@PathParam("id") int id, Comment comment) {
        commentLocal.updateComment(id, comment);
        return Response.status(Response.Status.CREATED).build();

    }

    @DELETE
    @Transactional
    @Path("/comments/{id}")
    public Response deleteComment(@PathParam("id") int id) {

        commentLocal.removeComment(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @GET
    @Path("/getUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserResponse(@PathParam("id") int id) {

        Users user = userLocal.getUserById(id);

        UserResponse userResponse = getUserResponseById(id);

        getUserPosts(user, userResponse);

        return Response.status(Response.Status.OK).entity(userResponse).build();
    }


    private UserResponse getUserResponseById(int id) {
        Users user = userLocal.getUserById(id);

        UserResponse userResponse = new UserResponse();

        if (user != null) {
            userResponse.setId(user.getId());
            userResponse.setMail(user.getMail());
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
        }

        return userResponse;
    }

    private void getUserPosts(Users user, UserResponse userResponse) {

        List<UserPostResponse> userPostResponses = new ArrayList<>();
        List<CommentResponse> commentResponses = new ArrayList<>();
        List<Comment> comments;


        if (user.getPosts() != null) {
            for (UserPosts userPosts : user.getPosts()) {
                UserPostResponse postResponse = new UserPostResponse();
                postResponse.setContent(userPosts.getContent());
                postResponse.setId(userPosts.getId());
                postResponse.setTitle(userPosts.getTitle());

                comments = userPosts.getComments();

                if (comments != null) {
                    for (Comment comment : comments) {
                        CommentResponse response = new CommentResponse();
                        response.setContent(comment.getCommentContent());
                        response.setId(comment.getId());

                        commentResponses.add(response);

                        postResponse.setComments(commentResponses);
                    }

                    userPostResponses.add(postResponse);
                    userResponse.setPosts(userPostResponses);
                }
            }

        }
    }



}
