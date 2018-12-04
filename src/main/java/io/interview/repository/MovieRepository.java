package io.interview.repository;

import io.interview.api.Movie;

import java.util.List;

public interface MovieRepository {

    Movie findById(long id);
    List<Movie> findAll();
    Movie save(Movie movie);
    Movie delete(long id);
}
