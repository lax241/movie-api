package io.interview.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
@Data
public class Movie {

    private final static AtomicLong idGenerator = new AtomicLong(1);
    private Long movieId;
    @Length(min = 1, max = 255)
    private String movieTitle;
    @Min(1900)
    @Max(2100)
    private Integer movieReleaseYear;
    @Min(0)
    @Max(999)
    private Integer movieDuration;
    @Length(min = 2, max = 30)
    private String movieLanguage;
    @Length(max = 255)
    private String movieDescription;
    private List<Actor> movieActorList;
    private List<Director> movieDirectorList;
    private Genres movieGenres;
    private Rating movieRating;

    public Movie() {
        this.movieId = idGenerator.getAndIncrement();
    }

    @JsonIgnore
    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    @JsonProperty
    public Long getMovieId() {
        return movieId;
    }
}
