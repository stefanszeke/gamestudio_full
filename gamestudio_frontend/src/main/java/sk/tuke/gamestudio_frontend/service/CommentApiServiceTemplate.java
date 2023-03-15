package sk.tuke.gamestudio_frontend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio_frontend.entity.Comment;
import sk.tuke.gamestudio_frontend.service.interfaces.CommentService;
import sk.tuke.gamestudio_frontend.service.other.CommentException;

import java.util.List;

public class CommentApiServiceTemplate implements CommentService {

    @Value("${api.url}/comment")
    String APIURL;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addComment(Comment comment) throws CommentException {
        restTemplate.postForObject(APIURL, comment, Comment.class);
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        ResponseEntity<List<Comment>> response = restTemplate.exchange(
                APIURL + "/" + game,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Comment>>() {
                });
        return response.getBody();
    }

    @Override
    public void reset() throws CommentException {
        restTemplate.delete(APIURL);
    }
}
