package com.unibuc.auclicenta.data;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class TestEntityQuery {
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private final String index = "test";

    public String createDocument(TestEntity testEntity) throws IOException {
        IndexResponse indexResponse = elasticsearchClient.index(i -> i
                .index(index)
                .id(testEntity.getId())
                .document(testEntity)
        );

        return "Ok";
    }
}
