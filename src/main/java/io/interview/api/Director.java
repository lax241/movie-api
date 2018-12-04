package io.interview.api;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class Director {

    @NotNull
    private Long directorId;
    @NotBlank
    @Length(min = 2, max = 255)
    private String directorFirstName;
    @NotBlank
    @Length(min = 2, max = 255)
    private String directorLastName;
}
