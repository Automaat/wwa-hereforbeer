package com.hereforbeer.controllers.exchanges;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data(staticConstructor = "of")
public class OfferGapDTO {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("y")
    private final Integer y;

}
