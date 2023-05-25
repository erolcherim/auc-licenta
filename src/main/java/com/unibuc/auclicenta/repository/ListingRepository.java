package com.unibuc.auclicenta.repository;

import com.unibuc.auclicenta.data.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface ListingRepository extends ElasticsearchRepository<Listing, String> {
   // @Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    Page<Listing> findByName(String name, Pageable pageable);
    List<Listing> findByIsActive(Boolean isActive);

    @Query("{\n" +
            "    \"function_score\": {\n" +
            "      \"query\": {\n" +
            "        \"bool\": {\n" +
            "          \"should\": [\n" +
            "            {\n" +
            "              \"match\": {\n" +
            "                \"name\": \"dell xps\"\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      },\n" +
            "      \"functions\": [\n" +
            "        {\n" +
            "          \"script_score\": {\n" +
            "            \"script\": {\n" +
            "              \"source\": \"decayNumericLinear(params.origin, params.scale, params.offset, params.decay,doc['currentPrice'].value)\", \"params\":{\"origin\":1500, \"scale\":1500.0, \"decay\":0.5, \"offset\":0}\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      ],\n" +
            "      \"boost_mode\": \"sum\"\n" +
            "    }\n" +
            "  }")
    Page<Listing> customFindQuery(String name, Integer currentPrice, Pageable pageable); //find most relevant by name in price range
}
