package com.hereforbeer.search_phrases;

import com.hereforbeer.controllers.exchanges.CategoryDTO;
import com.hereforbeer.controllers.exchanges.TrendDTO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class CategoriesService {

    private static final String MAIN_AGGREGATE = "visits";
    private static final String V_DATE = "v_date";
    private static final String PV_SUM_AGGREGATE = "pv_sum";
    private static final String VISIT_SUM_AGGREGATE = "visit_sum";
    private static final String PV_COUNT = "pv_count";
    private static final String VISIT_COUNT = "visit_count";

    private final SearchPhraseRepository searchPhraseRepository;
    private final CategoryTreeRepository categoryTreeRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public CategoriesService(SearchPhraseRepository searchPhraseRepository,
                             CategoryTreeRepository categoryTreeRepository,
                             ElasticsearchTemplate elasticsearchTemplate) {
        this.searchPhraseRepository = searchPhraseRepository;
        this.categoryTreeRepository = categoryTreeRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
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

    public TrendDTO getTrends(String searchPhrase) {
        SearchQuery query = aggregateViewsAndSearchesForSpecifiedSearchPhraseQuery(searchPhrase);

        return queryAndProcessResults(query);
    }

    private NativeSearchQuery aggregateViewsAndSearchesForSpecifiedSearchPhraseQuery(String searchPhrase) {
        return new NativeSearchQueryBuilder()
                .withQuery(matchQuery("search_phrase", searchPhrase))
                .withSort(SortBuilders
                        .fieldSort(V_DATE)
                        .unmappedType("date")
                        .order(SortOrder.ASC))
                .addAggregation(AggregationBuilders.terms(MAIN_AGGREGATE).field(V_DATE)
                        .subAggregation(
                                AggregationBuilders.sum(PV_SUM_AGGREGATE).field(PV_COUNT)
                        )
                        .subAggregation(
                                AggregationBuilders.sum(VISIT_SUM_AGGREGATE).field(VISIT_COUNT)
                        ))
                .build();
    }

    private SearchQuery mostRelevantCategoryQuery(@RequestParam("searchPhrase") String searchPhrase) {
        return new NativeSearchQueryBuilder()
                .withQuery(matchQuery("search_phrase", searchPhrase))
                .withSort(SortBuilders
                        .fieldSort(PV_COUNT)
                        .order(SortOrder.DESC))
                .withSort(SortBuilders
                        .fieldSort(VISIT_COUNT)
                        .order(SortOrder.DESC))
                .withIndices("search_phrases-2016-12-31")
                .withPageable(new PageRequest(0, 10))
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

    private TrendDTO queryAndProcessResults(SearchQuery query) {
        Aggregations aggregations = elasticsearchTemplate.query(query, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        //this probably won't work because of spring data api
        List<InternalSum> pvSums = (List<InternalSum>) aggregations.getAsMap().get(MAIN_AGGREGATE).getProperty(PV_SUM_AGGREGATE);
        List<InternalSum> visitsSums = (List<InternalSum>) aggregations.getAsMap().get(MAIN_AGGREGATE).getProperty(VISIT_SUM_AGGREGATE);

        List<Double> pvSumsMapped = removeEmptyEntriesAndMapToDoble(pvSums);
        List<Double> visitsSumsMapped = removeEmptyEntriesAndMapToDoble(visitsSums);
        List<Double> effectiveness = new ArrayList(pvSumsMapped.size());

        for (int i = 0; i < pvSumsMapped.size(); i++) {
            effectiveness.add(visitsSumsMapped.get(i) / pvSumsMapped.get(i));
        }

        return TrendDTO.of(pvSumsMapped, visitsSumsMapped, effectiveness);
    }

    private List<Double> removeEmptyEntriesAndMapToDoble(List<InternalSum> sums) {
        return sums.stream()
                .filter(item -> item.getValue() != 0)
                .map(InternalSum::getValue)
                .collect(Collectors.toList());
    }
}
