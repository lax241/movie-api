package io.interview.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.interview.MovieApplication;
import io.interview.MovieConfiguration;
import io.interview.MovieConstant;
import io.interview.api.Actor;
import io.interview.api.Movie;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MovieResourceIntegrationTest {

    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-movie.yml");
    public static final String ENDPOINT_URL = "http://localhost:8080" + MovieConstant.PATH_PREFIX + "/movies";

    @ClassRule
    public static final DropwizardAppRule<MovieConfiguration> RULE = new DropwizardAppRule<>(
            MovieApplication.class, CONFIG_PATH);

    @Before
    public void initialize() throws Exception{
        deleteAllEntity();
    }

    @Test
    public void testSwaggerUi() throws Exception {
        //when
        Response result = RULE.client().target("http://localhost:8080/swagger")
                .request().buildGet().invoke();
        //then
        assertThat(result.getStatus()).isEqualTo(200);
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
        Response deleteResponse = RULE.client().target(resourceUrl).request().buildDelete().invoke();

        //then
        assertThat(deleteResponse.getStatus()).isEqualTo(HttpStatus.OK_200);
    }

    @Test
    public void testGetMovieWithDurationQueryParams() throws Exception {
        //given
        final Movie movie = prepareRequestBody();
        movie.setMovieDuration(120);
        RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();
        movie.setMovieDuration(100);
        RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();
        movie.setMovieDuration(140);
        RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();

        //when
        String json = RULE.client()
                .target(ENDPOINT_URL + "?durationFrom=110&durationTo=130")
                .request().get(String.class);
        List<Movie> result = new ObjectMapper().readValue(json, new TypeReference<List<Movie>>() {
        });

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getMovieDuration()).isEqualTo(120);
    }

    @Test
    public void testGetMovieWithReleaseYearQueryParams() throws Exception {
        //given
        final Movie movie = prepareRequestBody();
        movie.setMovieReleaseYear(2000);
        RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();
        movie.setMovieReleaseYear(2001);
        RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();
        movie.setMovieReleaseYear(2002);
        RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();

        //when
        String json = RULE.client()
                .target(ENDPOINT_URL+"?releaseYearFrom=2001&releaseYearTo=2005")
                .request().get(String.class);
        List<Movie> result = new ObjectMapper().readValue(json, new TypeReference<List<Movie>>() {
        });

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getMovieReleaseYear()).isEqualTo(2001);
    }

    @Test
    public void testGetMovieWithActorQueryParams() throws Exception {
        //given
        final Movie movie = prepareRequestBody();
        Actor actor = new Actor();

        actor.setActorFirstName("AAA");
        actor.setActorLastName("bbb");
        movie.getMovieActorList().add(actor);
        RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();

        actor.setActorFirstName("ccc");
        actor.setActorLastName("DDD");
        movie.getMovieActorList().clear();
        movie.getMovieActorList().add(actor);
        RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();

        //when
        String json = RULE.client()
                .target(ENDPOINT_URL+"?actorFirstName=ccc&actorLastName=DDD")
                .request().get(String.class);
        List<Movie> result = new ObjectMapper().readValue(json, new TypeReference<List<Movie>>() {});

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getMovieActorList().get(0).getActorFirstName()).isEqualTo("ccc");
        assertThat(result.get(0).getMovieActorList().get(0).getActorLastName()).isEqualTo("DDD");
    }

    @Test
    public void testGetMovieWithComplexQueryParams() throws Exception {
        //given
        final Movie movie = prepareRequestBody();
        RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();

        Actor actor = new Actor();
        actor.setActorFirstName("xxx");
        actor.setActorLastName("ZZZ");
        movie.getMovieActorList().add(actor);
        movie.setMovieDuration(110);
        movie.setMovieReleaseYear(2005);
        RULE.client().target(ENDPOINT_URL).request().buildPost(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();

        //when
        String json = RULE.client()
                .target(ENDPOINT_URL+"?actorFirstName=xxx&actorLastName=ZZZ&releaseYearFrom=2001&releaseYearTo=2005&durationFrom=110&durationTo=130")
                .request().get(String.class);
        List<Movie> result = new ObjectMapper().readValue(json, new TypeReference<List<Movie>>() {});

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getMovieActorList().get(1).getActorFirstName()).isEqualTo("xxx");
        assertThat(result.get(0).getMovieActorList().get(1).getActorLastName()).isEqualTo("ZZZ");
        assertThat(result.get(0).getMovieReleaseYear()).isEqualTo(2005);
        assertThat(result.get(0).getMovieDuration()).isEqualTo(110);
    }

    @Test
    public void testGetNonExistMovie() throws Exception {
         //when
        Response result = RULE.client()
                .target(ENDPOINT_URL+"/423")
                .request().buildGet().invoke();
        //then
        assertThat(result.getStatus()).isEqualTo(404);
    }

    @Test
    public void testDeleteNonExistMovie() throws Exception {
        //when
        Response result = RULE.client()
                .target(ENDPOINT_URL+"/423")
                .request().buildDelete().invoke();
        //then
        assertThat(result.getStatus()).isEqualTo(404);
    }

    @Test
    public void testPutNonExistMovie() throws Exception {
        //given
        final Movie movie = prepareRequestBody();
        //when
        Response result = RULE.client().target(ENDPOINT_URL+"/423").request().buildPut(Entity.entity(movie, MediaType.APPLICATION_JSON_TYPE)).invoke();
        //then
        assertThat(result.getStatus()).isEqualTo(404);
    }

    private Movie prepareRequestBody() throws java.io.IOException {
        return new ObjectMapper().readValue(new File(ResourceHelpers.resourceFilePath("json/movie.json")), Movie.class);
    }

    private void deleteAllEntity() throws Exception {
        String json = RULE.client()
                .target(ENDPOINT_URL)
                .request().get(String.class);
        List<Movie> result = new ObjectMapper().readValue(json, new TypeReference<List<Movie>>() {});
        result.forEach(movie -> RULE.client().target(ENDPOINT_URL + "/" + movie.getMovieId()).request().buildDelete().invoke());
    }
}
