package com.unibuc.auclicenta.repository;

import com.unibuc.auclicenta.data.listing.Listing;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface ListingRepository extends ElasticsearchRepository<Listing, String> {
    @Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    SearchHits<Listing> findByName(String name);
}
