package io.interview.api;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class Actor {

    private final static AtomicLong idGenerator = new AtomicLong(1);

    private Long actorId;
    @NotBlank
    @Length(min=2, max=255)
    private String actorFirstName;
    @NotBlank
    @Length(min=2, max=255)
    private String actorLastName;

    public Actor() {
        this.actorId = idGenerator.incrementAndGet();
    }

}
