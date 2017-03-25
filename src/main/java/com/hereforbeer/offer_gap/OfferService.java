package com.hereforbeer.offer_gap;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OfferService {

    @Autowired
    private OfferGapRepository repository;

    private static final int LAST_DAYS = 5;

    @GetMapping("/")
    public List<OfferGaps> get() {

        SearchQuery query = new NativeSearchQueryBuilder()
                .withSort(SortBuilders
                        .fieldSort("score")
                        .order(SortOrder.DESC))
                .withIndices("offer_gaps-2017-01-28")
                .withPageable(new PageRequest(0,10))
                .build();


        return repository.search(query).getContent();
    }

}
