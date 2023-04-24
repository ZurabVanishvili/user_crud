package com.example.service;

import com.example.usercrud.entity.UserPosts;
import com.example.usercrud.model.UserPostsResponse;
import com.example.usercrud.model.UserResponse;
import com.example.usercrud.proxy.UserPostProxySession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@RequestScoped
@Path("posts")
public class PostService {

    @Inject
    private UserPostProxySession userPostProxySession;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserPostsResponse> getAllPosts(
            @DefaultValue("10") @QueryParam("pageSize") int pageSize,
            @DefaultValue("1") @QueryParam("pageNumber") int pageNumber
    ) {
        int start = (pageNumber - 1) * pageSize;

        return userPostProxySession.getAllPosts(start, pageSize);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public UserPostsResponse getPostById(@PathParam("id") int id) {
        return userPostProxySession.getPostById(id);
    }

    @POST
//    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserPostsResponse addPostToUser(@Context HttpServletRequest request, UserPosts post) {
        UserResponse user = (UserResponse) request.getAttribute("user");
        return userPostProxySession.addPostToUser(user.getId(), post);
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserPostsResponse updatePostOfUser(@PathParam("id") int id, UserPosts post, @Context HttpServletRequest request) {

        UserResponse response = (UserResponse) request.getAttribute("user");
        return userPostProxySession.updatePostOfUser(id, post, response);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserPostsResponse deletePost(@PathParam("id") int id, @Context HttpServletRequest request) {

        UserResponse response = (UserResponse) request.getAttribute("user");
        return userPostProxySession.deletePost(id,response);
    }
}
