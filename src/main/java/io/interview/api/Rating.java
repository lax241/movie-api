package io.interview.api;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class Rating {

    private Long id;
    @Min(1)
    @Max(10)
    private Integer rating;
    @Min(0)
    private Integer numberOfRatings;
}
