package io.interview.resources.params;

import lombok.Data;

import javax.ws.rs.QueryParam;

@Data
public class MovieFilterParam {

    @QueryParam("releaseYearFrom")
    private Integer releaseYearStart;

    @QueryParam("releaseYearTo")
    private Integer releaseYearTo;

    @QueryParam("durationFrom")
    private Integer durationFrom;

    @QueryParam("durationTo")
    private Integer durationTo;

    @QueryParam("actorFirstName")
    private String actorFirstName;

    @QueryParam("actorLastName")
    private String actorLastName;

}
