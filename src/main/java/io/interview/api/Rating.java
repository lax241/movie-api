package io.interview.api;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class Rating {

    private final static AtomicLong idGenerator = new AtomicLong(1);
    private Long ratingId;
    @Min(1)
    @Max(10)
    private Integer rating;
    @Min(0)
    private Integer numberOfRatings;

    public Rating() {
        this.ratingId = idGenerator.getAndIncrement();
    }
}
