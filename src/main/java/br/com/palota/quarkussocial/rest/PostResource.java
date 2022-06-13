package br.com.palota.quarkussocial.rest;

import br.com.palota.quarkussocial.domain.model.Post;
import br.com.palota.quarkussocial.domain.model.User;
import br.com.palota.quarkussocial.domain.repository.PostRepository;
import br.com.palota.quarkussocial.domain.repository.UserRepository;
import br.com.palota.quarkussocial.rest.dto.CreatePostRequest;
import br.com.palota.quarkussocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepository;
    private PostRepository postRepository;

    @Inject
    public PostResource(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest postRequest) {
        User user = userRepository.findById(userId);
        if (user == null) return Response.status(Response.Status.NOT_FOUND).build();
        Post post = new Post();
        post.setText(postRequest.getText());
        post.setUser(user);
        postRepository.persist(post);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPost(@PathParam("userId") Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) return Response.status(Response.Status.NOT_FOUND).build();
        PanacheQuery<Post> query = postRepository.find("user", Sort.by("dateTime", Sort.Direction.Descending), user);
        List<Post> list = query.list();
        List<PostResponse> response = list.stream().map(PostResponse::fromEntity).collect(Collectors.toList());
        return Response.ok(response).build();
    }
}
