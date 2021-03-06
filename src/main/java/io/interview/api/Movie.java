package io.interview.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
@Data
public class Movie {

    private Long movieId;
    @Length(min = 1, max = 255)
    @NotBlank
    private String movieTitle;
    @Min(1900)
    @Max(2100)
    private Integer movieReleaseYear;
    @Min(0)
    @Max(999)
    private Integer movieDuration;
    @Length(min = 2, max = 30)
    @NotBlank
    private String movieLanguage;
    @Length(max = 255)
    private String movieDescription;
    private List<Actor> movieActorList;
    private List<Director> movieDirectorList;
    private Genres movieGenres;
    private Rating movieRating;


    @JsonIgnore
    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    @JsonProperty
    public Long getMovieId() {
        return movieId;
    }
}
