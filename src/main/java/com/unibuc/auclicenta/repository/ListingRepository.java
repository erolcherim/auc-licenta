package com.unibuc.auclicenta.repository;

import com.unibuc.auclicenta.data.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;


public interface ListingRepository extends ElasticsearchRepository<Listing, String> {
    @Query("{\"match_phrase_prefix\": {\"name\": {\"query\": \"?0\"}}}")
        //TODO add isActive = 1 and recheck if all queries work
    Page<Listing> findByName(String name, Pageable pageable);

    Optional<Listing> findByIdAndIsActive(String id, int isActive);

    List<Listing> findByIsActive(int isActive);

    Page<Listing> findByIsActiveOrderByCreatedDateDesc(int isActive, Pageable pageable);

    @Query("{\n" +
            "    \"function_score\": {\n" +
            "      \"query\": {\n" +
            "        \"bool\": {\n" +
            "          \"should\": [\n" +
            "            {\n" +
            "              \"match\": {\n" +
            "                \"name\": \"?0\"\n" +
            "              }\n" +
            "            }\n" +
            "          ],\n" +
            "          \"filter\":{\n" +
            "            \"term\": {\"isActive\": \"2\"}\n" + //TODO: modify
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      \"functions\": [\n" +
            "        {\n" +
            "          \"script_score\": {\n" +
            "            \"script\": {\n" +
            "              \"source\": \"decayNumericLinear(params.origin, params.scale, params.offset, params.decay,doc['currentPrice'].value)\",\n" +
            "              \"params\": {\n" +
            "                \"origin\": ?1,\n" +
            "                \"scale\": ?1,\n" +
            "                \"decay\": 0.5,\n" +
            "                \"offset\": 0\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      ],\n" +
            "      \"boost_mode\": \"sum\"\n" +
            "    }\n" +
            "  }")
    Page<Listing> findByNameNearPrice(String name, int currentPrice, Pageable pageable); //find most relevant by name near suggested price
}
