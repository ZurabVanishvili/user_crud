package com.example.usercrud.service;

import com.example.usercrud.api.UserLocal;
import com.example.usercrud.api.UserPostLocal;
import com.example.usercrud.model.Comment;
import com.example.usercrud.model.UserPosts;
import com.example.usercrud.model.Users;
import com.example.usercrud.response.CommentResponse;
import com.example.usercrud.response.UserPostsResponse;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Path("posts")
public class PostService {

    @EJB
    private UserPostLocal userPostLocal;

    @EJB
    private UserLocal userLocal;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPosts() {

        List<UserPostsResponse> postsResponses = getAllPostResponse(userPostLocal.getAllPosts());
        return Response.status(Response.Status.OK).entity(postsResponses).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getPostById(@PathParam("id") int id) {

        UserPosts post = userPostLocal.getPostById(id);
        if (post == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<CommentResponse> commentResponses = new ArrayList<>();


        for (Comment comment : post.getComments()) {
            CommentResponse commentResponse = new CommentResponse(comment.getId(), comment.getCommentContent(), post.getId());

            commentResponses.add(commentResponse);

        }

        UserPostsResponse response = new UserPostsResponse(post.getId(), post.getContent(), post.getTitle(),commentResponses);

        return Response.status(Response.Status.OK).entity(response).build();


    }

    @POST
    @Path("{id}")
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
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updatePostOfUser(@PathParam("id") int id, UserPosts post) {

        userPostLocal.updatePost(id, post);

        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deletePost(@PathParam("id") int id) {

        userPostLocal.deleteUserPost(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }


    static List<UserPostsResponse> getUserPostsResponses(Users user) {

        List<UserPostsResponse> userPostsResponse = new ArrayList<>();
        List<CommentResponse> commentResponses;
        List<Comment> comments;

        if (user.getPosts() != null) {
            for (UserPosts userPosts : user.getPosts()) {

                comments = userPosts.getComments();
                commentResponses=new ArrayList<>();

                if (comments != null) {
                    for (Comment comment : comments) {
                        CommentResponse commentResponse = new CommentResponse(
                                comment.getId(), comment.getCommentContent(), comment.getPosts().getId()
                        );

                        if (comment.getPosts().getId() == userPosts.getId()) {
                            commentResponses.add(commentResponse);
                        }
                    }
                    UserPostsResponse postResponse = new UserPostsResponse(
                            userPosts.getId(), userPosts.getContent(), userPosts.getTitle(), commentResponses
                    );

                    userPostsResponse.add(postResponse);

                }

            }

        }
        return userPostsResponse;
    }

    private List<UserPostsResponse> getAllPostResponse(List<UserPosts> posts){

        UserPostsResponse response;
        CommentResponse commentResponse;
        List<CommentResponse> commentResponseList ;
        List<Comment> comments;
        List<UserPostsResponse> postResponses = new ArrayList<>();

        for (UserPosts userPostsLocal : posts) {
            comments = userPostsLocal.getComments();
            commentResponseList=new ArrayList<>();

            for (Comment comment: comments){
                commentResponse = new CommentResponse(comment.getId(),comment.getCommentContent(),comment.getPosts().getId());
                commentResponseList.add(commentResponse);
            }
            response = new UserPostsResponse(userPostsLocal.getId(),
                    userPostsLocal.getContent(), userPostsLocal.getTitle(),commentResponseList);


            postResponses.add(response) ;
        }
        return postResponses;
    }
}
