package com.hereforbeer.offer_gap;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
interface OfferGapRepository extends ElasticsearchRepository<OfferGaps, String> {
}
