package com.example.service;

import com.example.usercrud.entity.User;
import com.example.usercrud.model.UserPostsResponse;
import com.example.usercrud.model.UserResponse;
import com.example.usercrud.proxy.UserProxySession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;


import java.util.List;

@RequestScoped
@Path("users")
public class UserService {

    @Inject
    private UserProxySession userProxySession;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/me")
    public UserResponse getUserResponse(@Context HttpServletRequest request) {
        UserResponse locUser = (UserResponse) request.getAttribute("user");
        return userProxySession.getUserById(locUser.getId());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserResponse> getAllUsers(
            @DefaultValue("10") @QueryParam("pageSize") int pageSize,
            @DefaultValue("1") @QueryParam("pageNumber") int pageNumber,
            @QueryParam("firstName") String firstName) {

        int start = (pageNumber - 1) * pageSize;
        return userProxySession.getAllUsers(start, pageSize, firstName);
    }

    @GET
    @Path("/myPosts")
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public List<UserPostsResponse> testPost(@Context HttpServletRequest request) {
        return (List<UserPostsResponse>) request.getAttribute("posts");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/addUser")
    public UserResponse addUser(User user) {
        return userProxySession.addUser(user);
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserResponse updateUser(User user, @Context HttpServletRequest request) {
        UserResponse locUser = (UserResponse) request.getAttribute("user");
        return userProxySession.updateUser(locUser.getId(), user);

    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserResponse deleteUser(@PathParam("id") int id) {
        return userProxySession.deleteUser(id);
    }

}
