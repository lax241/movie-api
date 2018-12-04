package io.interview.api;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class Director {

    private final static AtomicLong idGenerator = new AtomicLong(1);

    private Long directorId;
    @NotBlank
    @Length(min = 2, max = 255)
    private String directorFirstName;
    @NotBlank
    @Length(min = 2, max = 255)
    private String directorLastName;

    public Director() {
        this.directorId = idGenerator.incrementAndGet();
    }
}
