package com.hereforbeer.offer_gap;

import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.QueryBuilders.fuzzyQuery;

@Service
public class ScoreService {

    private static final String MAX_SCORE_TERM = "maxScoreTerm";
    private final OfferGapRepository offerGapRepository;

    @Autowired
    public ScoreService(OfferGapRepository offerGapRepository) {
        this.offerGapRepository = offerGapRepository;
    }

    public double getMaxScoreForPhrase(String searchPhrase) {
        return offerGapRepository.search(getScoreForPhraseQuery(searchPhrase)).getContent()
                .stream()
                .findFirst()
                .map(OfferGaps::getScore)
                .orElse(0.0);
    }

    private NativeSearchQuery getScoreForPhraseQuery(String searchPhrase) {
        return new NativeSearchQueryBuilder()
                .withQuery(fuzzyQuery("search_phrase", searchPhrase))
                .withSort(SortBuilders
                        .fieldSort("score")
                        .order(SortOrder.DESC))
                .withPageable(new PageRequest(0, 1))
                .build();
    }
}
