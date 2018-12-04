package io.interview.resources;

import io.dropwizard.jersey.params.LongParam;
import io.interview.MovieConstant;
import io.interview.api.Movie;
import io.interview.repository.MovieRepository;
import io.interview.resources.filters.MovieFilter;
import io.interview.resources.params.MovieFilterParam;
import io.swagger.annotations.Api;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api
@Path(MovieConstant.PATH_PREFIX + "/movies")
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {

    private final MovieRepository movieRepository;


    public MovieResource(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GET
    @Path("/{movieId}")
    public Movie get(@PathParam("movieId") LongParam movieId) {
        Movie movie = movieRepository.findById(movieId.get());
        if (movie != null) {
            return movie;
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @GET
    public List<Movie> get(@BeanParam MovieFilterParam movieFilterParam) {
        List<Movie> result = movieRepository.findAll();
        MovieFilter.filterByReleaseYear(movieFilterParam.getReleaseYearFrom(),movieFilterParam.getReleaseYearTo(), result);
        MovieFilter.filterByDuration(movieFilterParam.getDurationFrom(),movieFilterParam.getDurationTo(), result);
        MovieFilter.filterByActor(movieFilterParam.getActorFirstName(),movieFilterParam.getActorLastName(), result);
        return result;
    }

    @PUT
    @Path("/{movieId}")
    public Response put(@PathParam("movieId") LongParam movieId, @Valid Movie movie) {
        Movie result = movieRepository.save(movie);
        if (result == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        else {
            return Response.status(Response.Status.OK).build();
        }
    }

    @POST
    public Response post(@Valid Movie movie) {
        long resourceId = movieRepository.save(movie).getMovieId();
        return Response.status(Response.Status.CREATED).header("Location", MovieConstant.PATH_PREFIX + "/movies/" + resourceId).build();
    }

    @DELETE
    @Path("/{movieId}")
    public Response delete(@PathParam("movieId") LongParam movieId) {
        Movie movie = movieRepository.delete(movieId.get());
        if (movie == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        else
            return Response.status(Response.Status.OK).build();

    }


}
