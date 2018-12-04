package io.interview.api;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
@Data
public class Genres {

    @NotNull
    private Long genresId;
    @Length(min = 2, max = 20)
    @NotBlank
    private String genresTitle;
}
