package com.example.usercrud.service;

import com.example.usercrud.entity.UserPosts;

import com.example.usercrud.model.UserPostsResponse;
import com.example.usercrud.proxy.UserPostProxySession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Path("posts")
public class PostService {



    @Inject
    private UserPostProxySession userPostProxySession;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserPostsResponse> getAllPosts() {
        return userPostProxySession.getAllPosts();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public UserPostsResponse getPostById(@PathParam("id") int id) {
        return userPostProxySession.getPostById(id);
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserPostsResponse addPostToUser(@PathParam(("id")) int id, UserPosts post) {
       return userPostProxySession.addPostToUser(id,post);
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserPostsResponse updatePostOfUser(@PathParam("id") int id, UserPosts post) {
        return userPostProxySession.updatePostOfUser(id, post);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserPostsResponse deletePost(@PathParam("id") int id) {
        return userPostProxySession.deletePost(id);
    }



}
