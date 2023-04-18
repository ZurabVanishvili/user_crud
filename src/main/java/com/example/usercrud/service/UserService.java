package com.example.usercrud.service;

import com.example.usercrud.api.CommentLocal;
import com.example.usercrud.api.UserLocal;
import com.example.usercrud.api.UserPostLocal;
import com.example.usercrud.model.Comment;
import com.example.usercrud.model.UserPosts;
import com.example.usercrud.model.Users;
import com.example.usercrud.response.CommentResponse;
import com.example.usercrud.response.UserPostsResponse;
import com.example.usercrud.response.UsersResponse;
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
    public Response getAllUsers() {
        List<Users> users = userLocal.getAllUsers();
        List<UsersResponse> response = new ArrayList<>();
        for (Users usersLocal: users){
            UsersResponse usersResponse = getUserResponseById(usersLocal.getId());

            getUserPosts(usersLocal, usersResponse);

            response.add(usersResponse);
        }
        return Response.status(Response.Status.OK).entity(response).build();

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
    public Response getAllPosts() {
        List<UserPosts> userPosts =  userPostLocal.getAllPosts();
        List<UserPostsResponse> postResponses = null;

        for (UserPosts userPostsLocal: userPosts){
            UsersResponse usersResponse = getUserResponseById(userPostsLocal.getOwner().getId());

            postResponses = getUserPosts(userPostsLocal.getOwner(), usersResponse);
        }
        return Response.status(Response.Status.OK).entity(postResponses).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/posts/{id}")
    public Response getPostById(@PathParam("id") int id) {
        UserPosts post =  userPostLocal.getPostById(id);
        if (post==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<CommentResponse> commentResponses;

        UserPostsResponse response =new UserPostsResponse(post.getId(), post.getContent(), post.getTitle());

        for (Comment comment: post.getComments()){
            CommentResponse commentResponse =new CommentResponse(comment.getId(),comment.getCommentContent());

            commentResponses = new ArrayList<>();
            commentResponses.add(commentResponse);

            response.setComments(commentResponses);
        }

        return Response.status(Response.Status.OK).entity(response).build();


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
        List<Comment> comments = commentLocal.getAllComments();
        List<CommentResponse> response =  new ArrayList<>();

        for (Comment comment: comments){
            CommentResponse commentResponse = new CommentResponse(comment.getId(),comment.getCommentContent());
            response.add(commentResponse);
        }
        return Response.status(Response.Status.OK).
                entity(response).build();
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
        UsersResponse usersResponse = getUserResponseById(id);
        getUserPosts(user, usersResponse);
        return Response.status(Response.Status.OK).entity(usersResponse).build();
    }


    private UsersResponse getUserResponseById(int id) {
        Users user = userLocal.getUserById(id);

        UsersResponse usersResponse = null;

        if (user != null) {
            usersResponse = new UsersResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getMail());
        }

        return usersResponse;
    }

    private List<UserPostsResponse> getUserPosts(Users user, UsersResponse usersResponse) {

        List<UserPostsResponse> userPostsRespons = new ArrayList<>();
        List<CommentResponse> commentResponses = new ArrayList<>();
        List<Comment> comments;

        if (user.getPosts() != null) {
            for (UserPosts userPosts : user.getPosts()) {
                UserPostsResponse postResponse = new UserPostsResponse(userPosts.getId(),userPosts.getContent(), userPosts.getTitle());

                comments = userPosts.getComments();

                if (comments != null) {
                    for (Comment comment : comments) {
                        CommentResponse response = new CommentResponse();
                        response.setContent(comment.getCommentContent());
                        response.setId(comment.getId());

                        commentResponses.add(response);

                        postResponse.setComments(commentResponses);
                    }

                    userPostsRespons.add(postResponse);
                    usersResponse.setPosts(userPostsRespons);
                }
            }

        }
        return userPostsRespons;
    }



}
