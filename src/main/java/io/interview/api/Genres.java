package io.interview.api;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class Genres {

    private final static AtomicLong idGenerator = new AtomicLong(1);

    private Long genresId;

    @Length(min = 2, max = 20)
    private String genresTitle;

    public Genres() {
        this.genresId = idGenerator.incrementAndGet();
    }
}
