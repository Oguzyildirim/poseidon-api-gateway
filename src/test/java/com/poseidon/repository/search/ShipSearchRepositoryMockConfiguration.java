package com.poseidon.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ShipSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ShipSearchRepositoryMockConfiguration {

    @MockBean
    private ShipSearchRepository mockShipSearchRepository;

}
