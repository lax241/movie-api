package io.interview.api;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class Genres {

    @NotNull
    private Long genresId;
    @Length(min = 2, max = 20)
    @NotBlank
    private String genresTitle;
}
