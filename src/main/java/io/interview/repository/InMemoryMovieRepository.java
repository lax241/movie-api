package io.interview.repository;

import io.interview.api.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryMovieRepository implements MovieRepository {

    private Map<Long, Movie> movieMap = new HashMap<>();

    @Override
    public Movie findById(long id) {
        return movieMap.get(id);
    }

    @Override
    public List<Movie> findAll() {
        return new ArrayList<>(movieMap.values());
    }

    @Override
    public Movie save(Movie movie) {
        if (movieMap.get(movie.getMovieId()) != null) {
            return movieMap.replace(movie.getMovieId(), movie);
        }
        movieMap.put(movie.getMovieId(), movie);
        return movie;
    }

    @Override
    public Movie delete(long id) {
        return movieMap.remove(id);
    }

}
