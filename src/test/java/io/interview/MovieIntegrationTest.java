package io.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.interview.api.Movie;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MovieIntegrationTest {

    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-movie.yml");

    @ClassRule
    public static final DropwizardAppRule<MovieConfiguration> RULE = new DropwizardAppRule<>(
            MovieApplication.class, CONFIG_PATH);
    public static final String ENDPOINT_URL = "http://localhost:8080" +  MovieConstant.PATH_PREFIX + "/movies";


    @Test
    public void testSwagger() throws Exception {
        //when
        Invocation invocation = RULE.client().target("http://localhost:8080/swagger")
                .request().buildGet();
        //then
        assertThat(invocation.invoke().getStatus()).isEqualTo(200);
    }

    @Test
    public void testPostMovie() throws Exception {
        //given
        final Movie movie = prepareRequestBody();

        //when
        Response response = RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaderString("Location")).contains(ENDPOINT_URL + "/");
    }

    @Test
    public void testUpdateMovie() throws Exception {
        //given
        final Movie movie = prepareRequestBody();

        //when
        Response postResponse = RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();
        String resourceUrl = postResponse.getHeaderString("Location");
        movie.setMovieDuration(123);
        Response putResponse = RULE.client().target(resourceUrl).request().buildPut(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();

        //then
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.OK_200);
    }

    @Test
    public void testDeleteMovie() throws Exception {
        //given
        final Movie movie = prepareRequestBody();

        //when
        Response postResponse = RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();
        String resourceUrl = postResponse.getHeaderString("Location");
        movie.setMovieDuration(123);
        Response putResponse = RULE.client().target(resourceUrl).request().buildDelete().invoke();

        //then
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.OK_200);
    }

    @Test
    public void testGetMovieWithDurationQueryParam() throws Exception {
        //given
        final Movie movie = prepareRequestBody();

        //when
        Response postResponse = RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();
        String resourceUrl = postResponse.getHeaderString("Location");
        movie.setMovieDuration(123);
        Response putResponse = RULE.client().target(resourceUrl).request().buildDelete().invoke();

        //then
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.OK_200);
    }

    private Movie prepareRequestBody() throws java.io.IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(ResourceHelpers.resourceFilePath("json/createMovie.json")), Movie.class);
    }

}
