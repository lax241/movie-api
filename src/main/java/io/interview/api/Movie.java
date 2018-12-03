package io.interview.api;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Data
public class Movie {

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
    private List<Actor> actorList;
    private List<Director> directorList;
    private Genres genres;
    private Rating rating;

}
