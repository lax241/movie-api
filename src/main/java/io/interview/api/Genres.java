package io.interview.api;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class Genres {

    private Long genresId;

    @Length(min = 2, max = 20)
    private String genresTitle;

    public Genres() {
    }
}
