package io.interview.api;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class Actor {

    @NotNull
    private Long actorId;
    @NotBlank
    @Length(min=2, max=255)
    private String actorFirstName;
    @NotBlank
    @Length(min=2, max=255)
    private String actorLastName;

}
