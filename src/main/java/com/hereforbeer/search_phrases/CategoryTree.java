package com.hereforbeer.search_phrases;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Data
@Document(indexName = "category_tree-2016-12-31", type = "categoryTree")

class CategoryTree {

    @Id
    private String id;

    @JsonProperty("category_id")
    private final String categoryId;

    @JsonProperty("category_name")
    private final String categoryName;

    @JsonProperty("name_path")
    private final List<String> namePath;
}
