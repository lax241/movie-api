package io.interview.api;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class Actor {

    private Long actorId;
    @NotBlank
    @Length(min=2, max=255)
    private String actorFirstName;
    @NotBlank
    @Length(min=2, max=255)
    private String actorLastName;

}
