package com.hereforbeer.offer_gap;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "offer_gaps-2017-01-27", type = "offer_gaps")
public class OfferGaps {


    @Id
    private String id;

    @JsonProperty("searches_count")
    private int searchesCount;

    @JsonProperty("offers_count")
    private int offersCount;

    @JsonProperty("search_phrase")
    private String searchPhrase;

    @JsonProperty("v_date")
    private String vDate;

    private double score;

}
