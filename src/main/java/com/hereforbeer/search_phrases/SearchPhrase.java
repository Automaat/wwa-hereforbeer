package com.hereforbeer.search_phrases;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "search_phrases-2016-12-31", type = "search_phrases")
class SearchPhrase {
    @Id
    private String id;

    @JsonProperty("category_tree")
    private String categoryTree;

    @JsonProperty("v_date")
    private String vDate;

    @JsonProperty("search_phrase")
    private String searchPhrase;

    @JsonProperty("pv_count")
    private int pvCount;

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("visit_count")
    private int visitCount;
}
