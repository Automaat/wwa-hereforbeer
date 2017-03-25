package com.hereforbeer.offer_gap;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "offer_gaps-2017-01-27", type = "offer_gaps")
public class OfferGaps {


    private int searches_count;
    private int offers_count;
    @Id
    private String id;
    @JsonProperty("search_phrase")
    private String searchPhrase;
    private double score;
    private String v_date;
}
