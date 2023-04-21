package com.example.service;

import com.example.usercrud.entity.Comment;
import com.example.usercrud.model.CommentResponse;
import com.example.usercrud.proxy.CommentProxySession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@RequestScoped
@Path("comments")
public class CommentService {



    @Inject
    private CommentProxySession commentProxySession;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CommentResponse> getAllComments(
            @DefaultValue("10")  @QueryParam("pageSize") int pageSize,
            @DefaultValue("1")  @QueryParam("pageNumber") int pageNumber
    ) {
        int start= (pageNumber-1)*pageSize;

        return commentProxySession.getAllComments(start,pageSize);

    }

    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public CommentResponse addComment(@PathParam("id") int id, Comment comment) {
        return commentProxySession.addComment(id, comment);
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public CommentResponse updateComment(@PathParam("id") int id, Comment comment) {
        return commentProxySession.updateComment(id,comment);

    }

    @DELETE
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public CommentResponse deleteComment(@PathParam("id") int id) {
       return commentProxySession.deleteComment(id);
    }
}
