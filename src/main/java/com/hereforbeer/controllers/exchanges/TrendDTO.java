package com.hereforbeer.controllers.exchanges;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data(staticConstructor = "of")
public class TrendDTO {

    @JsonProperty("searches_count")
    private final List<Double> pvSums;

    @JsonProperty("visits_count")
    private final List<Double> visitSums;

    @JsonProperty("effectiveness")
    private final List<Double> effectiveness;

}
