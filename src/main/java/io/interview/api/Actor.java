package io.interview.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty
    public Long getActorId() {
        return actorId;
    }

    @JsonIgnore
    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

}
