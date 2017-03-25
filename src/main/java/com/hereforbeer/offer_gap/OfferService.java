package com.hereforbeer.offer_gap;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;

@RestController
public class OfferService {

    private static final int MAX_SCORE = 10;
    private static final int LAST_DAYS = 2;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final NativeSearchQueryBuilder QUERY_TEMPLATE = new NativeSearchQueryBuilder()
            .withSort(SortBuilders
                    .fieldSort("score")
                    .order(SortOrder.DESC))
            .withPageable(new PageRequest(0, 10));

    private Map<OfferGaps, Integer> candidates = new ConcurrentHashMap<>();

    @Autowired
    private OfferGapRepository repository;

    @GetMapping("/offer-gaps")
    public List<OfferGaps> get() {

        LocalDateTime currentDate = now();

        for (int i = 0; i < LAST_DAYS; i++) {
            List<OfferGaps> bestGapsFromDay = repository.search(offerGapsForDate(currentDate)).getContent();

            int scoreForPosition = MAX_SCORE;

            for (OfferGaps offerGaps : bestGapsFromDay) {
                if (candidates.isEmpty()){
                    addCandidateWithScore(offerGaps, scoreForPosition);
                } else {
                    for (OfferGaps candidate : candidates.keySet()) {
                        if (offerGapRepeatsBasedOnSearchPhrase(candidate.getSearchPhrase(), offerGaps.getSearchPhrase())) {
                            addCandidateWithScore(candidate, candidates.get(candidate) + scoreForPosition);
                        } else {
                            addCandidateWithScore(offerGaps, scoreForPosition);
                        }
                    }
                }
                scoreForPosition--;
            }

            currentDate.minusDays(1);
        }

        return sortedCandidates();
    }

    private LocalDateTime now() {
        return LocalDateTime.of(2017, Month.JANUARY, 28, 0, 0);
    }

    private NativeSearchQuery offerGapsForDate(LocalDateTime actualDate) {
        return QUERY_TEMPLATE.withIndices("offer_gaps-" + actualDate.format(FORMATTER)).build();
    }

    private void addCandidateWithScore(OfferGaps candidate, int scoreForPosition) {
        candidates.put(candidate, scoreForPosition);
    }

    private boolean offerGapRepeatsBasedOnSearchPhrase(String firstPhrase, String secondPhrase) {
        return StringUtils.getLevenshteinDistance(firstPhrase, secondPhrase) < ((double) shorterStringLength(firstPhrase, secondPhrase) * 0.1);
    }

    private int shorterStringLength(String first, String second) {
        return first.length() < second.length() ? first.length() : second.length();
    }

    private List<OfferGaps> sortedCandidates() {
        return candidates.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
