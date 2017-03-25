package com.hereforbeer.controllers.exchanges;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data(staticConstructor = "of")
public class CategoryDTO {

    @JsonProperty("category_id")
    private final String categoryId;

    @JsonProperty("path")
    private final List<String> path;
}
