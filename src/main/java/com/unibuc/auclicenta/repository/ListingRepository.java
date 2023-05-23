package com.unibuc.auclicenta.repository;

import com.unibuc.auclicenta.data.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface ListingRepository extends ElasticsearchRepository<Listing, String> {
    //@Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    Page<Listing> findByName(String name, Pageable pageable);
}
