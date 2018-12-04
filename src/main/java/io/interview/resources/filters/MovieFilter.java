package io.interview.resources.filters;

import io.interview.api.Movie;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class MovieFilter {

    public static void filterByReleaseYear(Integer from, Integer to, List<Movie> result) throws WebApplicationException {
        if(from != null && to!= null && from > to){
            throw new WebApplicationException("Invalid release year range.", Response.Status.BAD_REQUEST);
        }
        if (from != null) {
            result.removeIf(movie -> movie.getMovieReleaseYear() < from);
        }
        if (to != null) {
            result.removeIf(movie -> movie.getMovieReleaseYear() > to);
        }
    }

    public static void filterByDuration(Integer from, Integer to, List<Movie> result) throws WebApplicationException {
        if(from != null && to!= null && from > to){
            throw new WebApplicationException("Invalid duration range.", Response.Status.BAD_REQUEST);
        }
        if (from != null) {
            result.removeIf(movie -> movie.getMovieDuration() < from);
        }
        if (to != null) {
            result.removeIf(movie -> movie.getMovieDuration() > to);
        }
    }

    public static void filterByActor(String firstName, String lastName, List<Movie> result) {
        if (!StringUtils.isBlank(firstName) && StringUtils.isBlank(lastName)) {
            result.removeIf(movie -> movie.getMovieActorList().stream().noneMatch(actor -> firstName.equalsIgnoreCase(actor.getActorFirstName())));
        }
        if (StringUtils.isBlank(firstName) && !StringUtils.isBlank(lastName)) {
            result.removeIf(movie -> movie.getMovieActorList().stream().noneMatch(actor -> lastName.equalsIgnoreCase(actor.getActorLastName())));
        }
        if (!StringUtils.isBlank(firstName) && !StringUtils.isBlank(lastName)) {
            result.removeIf(movie -> movie.getMovieActorList().stream().noneMatch(actor -> firstName.equalsIgnoreCase(actor.getActorFirstName()) && lastName.equalsIgnoreCase(actor.getActorLastName())));
        }
    }
}
