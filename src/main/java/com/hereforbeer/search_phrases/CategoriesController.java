package com.hereforbeer.search_phrases;

import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Controller
@RequestMapping("/categories")
class CategoriesController {

    private final SearchPhraseRepository searchPhraseRepository;

    private final CategoryTreeRepository categoryTreeRepository;

    @Autowired
    public CategoriesController(SearchPhraseRepository searchPhraseRepository, CategoryTreeRepository categoryTreeRepository) {
        this.searchPhraseRepository = searchPhraseRepository;
        this.categoryTreeRepository = categoryTreeRepository;
    }

    @GetMapping(params = {"searchPhrase"})
    ResponseEntity<?> getCategories(@RequestParam("searchPhrase") String searchPhrase) {
        SearchQuery query = mostRelevantCategoryQuery(searchPhrase);

        List<SearchPhrase> results = searchPhraseRepository.search(query).getContent();
        return results
                .stream()
                .findFirst()
                .flatMap(phrase -> Optional.ofNullable(phrase.getCategoryId()))
                .map(id -> ResponseEntity.ok(categoryById(id)))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private SearchQuery mostRelevantCategoryQuery(@RequestParam("searchPhrase") String searchPhrase) {
        return new NativeSearchQueryBuilder()
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
    }

    private CategoryDTO categoryById(String categoryId) {

        List<CategoryTree> results = categoryTreeRepository.search(getCategoryByIdQuery(categoryId)).getContent();

        List<String> path = results.stream()
                .findFirst()
                .map(CategoryTree::getNamePath)
                .orElse(Collections.emptyList());

        return CategoryDTO.of(categoryId, path);
    }

    private NativeSearchQuery getCategoryByIdQuery(String categoryId) {
        return new NativeSearchQueryBuilder()
                    .withQuery(matchQuery("category_id", categoryId))
                    .withPageable(new PageRequest(0, 1))
                    .build();
    }

}
