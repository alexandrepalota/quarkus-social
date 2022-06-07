package br.com.palota.quarkussocial.rest;

import br.com.palota.quarkussocial.rest.dto.CreateUserRequest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/users")
public class UserResource {

    @POST
    public Response createUser(CreateUserRequest userRequest) {
        return Response.ok().build();
    }
}
