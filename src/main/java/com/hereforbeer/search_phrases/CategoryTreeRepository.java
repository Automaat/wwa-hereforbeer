package com.hereforbeer.search_phrases;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CategoryTreeRepository extends ElasticsearchCrudRepository<CategoryTree, String>{

}
