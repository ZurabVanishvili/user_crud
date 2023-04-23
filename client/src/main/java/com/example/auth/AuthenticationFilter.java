package com.example.auth;


import com.example.usercrud.model.UserResponse;
import com.example.usercrud.proxy.UserProxySession;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private UserProxySession proxySession;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        String path = requestContext.getUriInfo().getPath();
        if (path.endsWith("/addUser")) {
            return;
        }

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {

            String base64Credentials = authorizationHeader.substring("Basic ".length()).trim();
            byte[] credentials = Base64.getDecoder().decode(base64Credentials);
            String usernameAndPassword = new String(credentials, StandardCharsets.UTF_8);
            String[] parts = usernameAndPassword.split(":", 2);
            String login = parts[0];
            String password = parts[1];

            UserResponse user = proxySession.getUserByLogin(login);



            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                requestContext.setProperty("user", user);
                return;
            }
        }
        throw new NotAuthorizedException("Invalid username or password");
    }
}
