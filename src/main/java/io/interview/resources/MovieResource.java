package io.interview.resources;

import io.dropwizard.jersey.params.LongParam;
import io.interview.MovieConstant;
import io.interview.api.Movie;
import io.interview.repository.MovieRepository;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.jvnet.hk2.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Api
@Path(MovieConstant.PATH_PREFIX + "/movies")
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {

    private final MovieRepository movieRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieResource.class);


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
    public List<Movie> get(@Optional @BeanParam MovieFilterParam paramBean) {
        List<Movie> result = movieRepository.findAll();
        if(paramBean.getReleaseYearStart() != null){
            result = result.stream().filter(it -> it.getMovieReleaseYear() >= paramBean.getReleaseYearStart()).collect(Collectors.toList());
        }
        if(paramBean.getReleaseYearTo() != null){
            result = result.stream().filter(it -> it.getMovieReleaseYear() <= paramBean.getReleaseYearTo()).collect(Collectors.toList());
        }
        if(paramBean.getDurationFrom() != null){
            result = result.stream().filter(it -> it.getMovieDuration() >= paramBean.getDurationFrom()).collect(Collectors.toList());
        }
        if(!StringUtils.isBlank(paramBean.getActorFirstName())){
            //TODO
        }
        if(!StringUtils.isBlank(paramBean.getActorLastName())){
            //TODO
        }
        return result;
    }

    @PUT
    @Path("/{movieId}")
    public Response put(@PathParam("movieId") LongParam movieId, @Valid Movie movie) {
        Movie result = movieRepository.update(movie);
        if (result == null)
            return Response.status(Response.Status.NO_CONTENT).build();
        else
            return Response.status(Response.Status.OK).build();
    }

    @POST
    public Response post(@Valid Movie movie) {
        movieRepository.save(movie);
        LOGGER.info("RECEIVE Movie");
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{movieId}")
    public Response delete(@PathParam("movieId") LongParam movieId) {
        Movie movie = movieRepository.delete(movieId.get());
        LOGGER.info("REMOVE Movie");
        if (movie == null)
            return Response.status(Response.Status.NO_CONTENT).build();
        else
            return Response.status(Response.Status.ACCEPTED).build();

    }


}
