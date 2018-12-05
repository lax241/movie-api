package io.interview.repository;

import io.interview.api.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {

    Optional<Movie> findById(long id);
    List<Movie> findAll();
    Movie save(Movie movie);
    Optional<Movie> delete(long id);
}
