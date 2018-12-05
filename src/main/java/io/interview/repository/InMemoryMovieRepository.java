package io.interview.repository;

import io.interview.api.Movie;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMovieRepository implements MovieRepository {

    private Map<Long, Movie> movieMap = new HashMap<>();
    private final static AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public Optional<Movie> findById(long id) {
        return Optional.ofNullable(movieMap.get(id));
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
        long id = idGenerator.getAndIncrement();
        movie.setMovieId(id);
        movieMap.put(id, movie);
        return movie;
    }

    @Override
    public Optional<Movie> delete(long id) {
        return Optional.ofNullable(movieMap.remove(id));
    }

}
