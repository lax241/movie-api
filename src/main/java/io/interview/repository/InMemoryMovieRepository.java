package io.interview.repository;

import io.interview.api.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryMovieRepository implements MovieRepository {

    private Map<Long, Movie> movieMap = new HashMap<>();

    public InMemoryMovieRepository(){
    }

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
        return movieMap.put(movie.getMovieId(), movie);
    }

    @Override
    public Movie delete(long id) {
        return movieMap.remove(id);
    }

    @Override
    public Movie update(Movie movie) {
        return movieMap.replace(movie.getMovieId(), movie);
    }
}
