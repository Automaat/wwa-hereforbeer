package com.hereforbeer.search_phrases;

import com.hereforbeer.controllers.exchanges.CategoryDTO;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class CategoriesService {

    private final SearchPhraseRepository searchPhraseRepository;
    private final CategoryTreeRepository categoryTreeRepository;

    @Autowired
    public CategoriesService(SearchPhraseRepository searchPhraseRepository, CategoryTreeRepository categoryTreeRepository) {
        this.searchPhraseRepository = searchPhraseRepository;
        this.categoryTreeRepository = categoryTreeRepository;
    }

    public Optional<CategoryDTO> getCategories(String searchPhrase) {
        SearchQuery query = mostRelevantCategoryQuery(searchPhrase);

        List<SearchPhrase> results = searchPhraseRepository.search(query).getContent();
        return results
                .stream()
                .findFirst()
                .flatMap(phrase -> Optional.ofNullable(phrase.getCategoryId()))
                .map(this::categoryById);
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
