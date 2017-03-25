package com.hereforbeer.search_phrases;

import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Controller
@RequestMapping("/categories")
public class CategoriesController {

    private final SearchPhraseRepository searchPhraseRepository;

    @Autowired
    public CategoriesController(SearchPhraseRepository searchPhraseRepository) {
        this.searchPhraseRepository = searchPhraseRepository;
    }

    @GetMapping(params = {"searchPhrase"})
    public ResponseEntity<?> getCategories(@RequestParam("searchPhrase") String searchPhrase) {
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("search_phrase", searchPhrase))
                .withSort(SortBuilders
                        .fieldSort("pv_count")
                        .order(SortOrder.DESC))
                .withSort(SortBuilders
                        .fieldSort("visit_count")
                        .order(SortOrder.DESC))
                .withIndices("search_phrases-2016-12-31")
                .withPageable(new PageRequest(0,10))
                .build();

        List<SearchPhrase> results = searchPhraseRepository.search(query).getContent();
        return results
                .stream()
                .findFirst()
                .map(phrase -> ResponseEntity.ok(new CategoryIdDTO(phrase.getCategoryId())))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
