package io.interview.resources.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.ResourceHelpers;
import io.interview.api.Movie;
import io.interview.resources.filters.MovieFilter;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import java.io.File;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MovieFilterTest {

    @Test
    public void testFilterByReleaseYearFrom() throws Exception {
        //given
        List<Movie> input = new ObjectMapper().readValue(new File(ResourceHelpers.resourceFilePath("json/movieList.json")),new TypeReference<List<Movie>>(){});

        //when
        MovieFilter.filterByReleaseYear(2000,null, input);

        //then
        assertThat(input.size()).isEqualTo(2);
        assertThat(input.get(0).getMovieReleaseYear()).isEqualTo(2000);
        assertThat(input.get(1).getMovieReleaseYear()).isEqualTo(2050);
    }

    @Test
    public void testFilterByReleaseYearTo() throws Exception {
        //given
        List<Movie> input = new ObjectMapper().readValue(new File(ResourceHelpers.resourceFilePath("json/movieList.json")),new TypeReference<List<Movie>>(){});

        //when
        MovieFilter.filterByReleaseYear(null,1999, input);

        //then
        assertThat(input.size()).isEqualTo(1);
        assertThat(input.get(0).getMovieReleaseYear()).isEqualTo(1950);
    }


    @Test
    public void testFilterByReleaseYearFromAndTo() throws Exception {
        //given
        List<Movie> input = new ObjectMapper().readValue(new File(ResourceHelpers.resourceFilePath("json/movieList.json")),new TypeReference<List<Movie>>(){});

        //when
        MovieFilter.filterByReleaseYear(2049,2051, input);

        //then
        assertThat(input.size()).isEqualTo(1);
        assertThat(input.get(0).getMovieReleaseYear()).isEqualTo(2050);
    }

    @Test(expected = WebApplicationException.class)
    public void testFilterByReleaseYearInvalidRange() throws Exception {
        //given
        List<Movie> input = new ObjectMapper().readValue(new File(ResourceHelpers.resourceFilePath("json/movieList.json")),new TypeReference<List<Movie>>(){});

        //when
        MovieFilter.filterByReleaseYear(2000,1998, input);
    }

    @Test
    public void testFilterByDurationFrom() throws Exception {
        //given
        List<Movie> input = new ObjectMapper().readValue(new File(ResourceHelpers.resourceFilePath("json/movieList.json")),new TypeReference<List<Movie>>(){});

        //when
        MovieFilter.filterByDuration(20,null, input);

        //then
        assertThat(input.size()).isEqualTo(2);
        assertThat(input.get(0).getMovieDuration()).isEqualTo(25);
        assertThat(input.get(1).getMovieDuration()).isEqualTo(120);
    }

    @Test
    public void testFilterByDurationTo() throws Exception {
        //given
        List<Movie> input = new ObjectMapper().readValue(new File(ResourceHelpers.resourceFilePath("json/movieList.json")),new TypeReference<List<Movie>>(){});

        //when
        MovieFilter.filterByDuration(null,2, input);

        //then
        assertThat(input.size()).isEqualTo(1);
        assertThat(input.get(0).getMovieDuration()).isEqualTo(1);
    }


    @Test
    public void testFilterByDurationFromAndTo() throws Exception {
        //given
        List<Movie> input = new ObjectMapper().readValue(new File(ResourceHelpers.resourceFilePath("json/movieList.json")),new TypeReference<List<Movie>>(){});

        //when
        MovieFilter.filterByDuration(50,150, input);

        //then
        assertThat(input.size()).isEqualTo(1);
        assertThat(input.get(0).getMovieDuration()).isEqualTo(120);
    }

    @Test(expected = WebApplicationException.class)
    public void testFilterByDurationInvalidRange() throws Exception {
        //given
        List<Movie> input = new ObjectMapper().readValue(new File(ResourceHelpers.resourceFilePath("json/movieList.json")),new TypeReference<List<Movie>>(){});

        //when
        MovieFilter.filterByDuration(50,25, input);
    }

    @Test
    public void testFilterByActorFirstName() throws Exception {
        //given
        List<Movie> input = new ObjectMapper().readValue(new File(ResourceHelpers.resourceFilePath("json/movieList.json")),new TypeReference<List<Movie>>(){});

        //when
        MovieFilter.filterByActor("exampleFirstName",null, input);

        //then
        assertThat(input.size()).isEqualTo(2);
        assertThat(input.get(0).getMovieActorList().get(0).getActorFirstName()).isEqualTo("exampleFirstName");
        assertThat(input.get(1).getMovieActorList().get(0).getActorFirstName()).isEqualTo("exampleFirstName");
    }


    @Test
    public void testFilterByActorLastName() throws Exception {
        //given
        List<Movie> input = new ObjectMapper().readValue(new File(ResourceHelpers.resourceFilePath("json/movieList.json")),new TypeReference<List<Movie>>(){});

        //when
        MovieFilter.filterByActor("","test2", input);

        //then
        assertThat(input.size()).isEqualTo(1);
        assertThat(input.get(0).getMovieActorList().get(1).getActorLastName()).isEqualTo("test2");
    }

    public void testFilterByActorName() throws Exception {
        //given
        List<Movie> input = new ObjectMapper().readValue(new File(ResourceHelpers.resourceFilePath("json/movieList.json")),new TypeReference<List<Movie>>(){});

        //when
        MovieFilter.filterByActor("AAA","BBB", input);

        //then
        assertThat(input.size()).isEqualTo(1);
        assertThat(input.get(0).getMovieActorList().get(0).getActorFirstName()).isEqualTo("AAA");
        assertThat(input.get(0).getMovieActorList().get(0).getActorLastName()).isEqualTo("BBB");
    }

    public void testFilterByActorEmptyResult() throws Exception {
        //given
        List<Movie> input = new ObjectMapper().readValue(new File(ResourceHelpers.resourceFilePath("json/movieList.json")),new TypeReference<List<Movie>>(){});

        //when
        MovieFilter.filterByActor("Cezary","Pazura", input);

        //then
        assertThat(input.size()).isEqualTo(0);
    }
}
