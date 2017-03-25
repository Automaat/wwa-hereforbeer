package com.hereforbeer.search_phrases;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SearchPhraseRepository extends ElasticsearchRepository<SearchPhrase, String> {
}
