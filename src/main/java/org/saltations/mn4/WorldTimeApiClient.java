package org.saltations.mn4;


import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

@Client("https://worldtimeapi.org/api")
public interface WorldTimeApiClient {

    @Get("/timezone")
    List<String> listTimezones();

    @Get("/timezone/{area}")
    List<String> listLocationsInArea(@NotBlank String area);

    @Get("/timezone/{area}/{location}")
    Map<String, Object> getTimeForLocation(@NotBlank String area, @NotBlank String location);
}
