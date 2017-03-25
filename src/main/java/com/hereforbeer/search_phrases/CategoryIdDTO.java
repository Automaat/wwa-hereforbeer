package com.hereforbeer.search_phrases;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CategoryIdDTO {

    @JsonProperty("category_id")
    private final String categoryId;
}
