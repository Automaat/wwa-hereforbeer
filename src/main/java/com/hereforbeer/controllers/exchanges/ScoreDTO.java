package com.hereforbeer.controllers.exchanges;

import lombok.Data;

@Data(staticConstructor = "of")
public class ScoreDTO {
    private final Double score;
}
