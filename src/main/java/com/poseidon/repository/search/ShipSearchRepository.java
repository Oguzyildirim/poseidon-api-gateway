package com.poseidon.repository.search;

import com.poseidon.domain.Ship;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Ship entity.
 */
public interface ShipSearchRepository extends ElasticsearchRepository<Ship, Long> {
}
