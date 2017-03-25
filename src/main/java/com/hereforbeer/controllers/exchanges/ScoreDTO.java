package com.hereforbeer.controllers.exchanges;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data(staticConstructor = "of")
public class ScoreDTO {

    @JsonProperty("score")
    private final Double score;
}
