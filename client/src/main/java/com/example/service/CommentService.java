package com.example.service;

import com.example.usercrud.entity.Comment;
import com.example.usercrud.model.CommentResponse;
import com.example.usercrud.model.UserResponse;
import com.example.usercrud.proxy.CommentProxySession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@RequestScoped
@Path("comments")
public class CommentService {


    @Inject
    private CommentProxySession commentProxySession;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("myComments")
    public List<CommentResponse> getMyComments(@Context HttpServletRequest request){
        UserResponse user = (UserResponse) request.getAttribute("user");
        return commentProxySession.getAllCommentsOfUser(user.getId());

    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CommentResponse> getAllComments(
            @DefaultValue("10") @QueryParam("pageSize") int pageSize,
            @DefaultValue("1") @QueryParam("pageNumber") int pageNumber) {

        int start = (pageNumber - 1) * pageSize;
        return commentProxySession.getAllComments(start, pageSize);

    }

    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public CommentResponse addComment(@PathParam("id") int id, Comment comment, @Context HttpServletRequest request) {
        UserResponse user = (UserResponse) request.getAttribute("user");
        return commentProxySession.addComment(id, user.getId(), comment);
    }


    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public CommentResponse updateComment(@PathParam("id") int id, Comment comment, @Context HttpServletRequest request) {
        UserResponse user = (UserResponse) request.getAttribute("user");
        return commentProxySession.updateComment(id, comment, user.getId());
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    @SuppressWarnings("unchecked")
    public CommentResponse deleteComment(@PathParam("id") int id, @Context HttpServletRequest request) {

        List<CommentResponse> commentResponses = (List<CommentResponse>) request.getAttribute("comments");

        if (commentResponses!=null) {
            for (CommentResponse commentResponse : commentResponses) {
                if (commentResponse.getId() == id) {
                    return commentProxySession.deleteComment(id);
                } else {

                    try {
                        throw new IllegalAccessException("This comment doesn't belong to you");
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
        }
        throw new NotFoundException("Comment not found");

    }
}
