package io.interview.api;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class Rating {

    @NotNull
    private Long ratingId;
    @Min(1)
    @Max(10)
    private Integer rating;
    @Min(0)
    private Integer numberOfRatings;
}
