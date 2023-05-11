package com.unibuc.auclicenta.config;

import com.mongodb.lang.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.config.EnableElasticsearchAuditing;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.unibuc.auclicenta.repository.listing")
@EnableElasticsearchAuditing
public class EsConfiguration extends ElasticsearchConfiguration {
    @NonNull
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
//                .connectedTo("elasticsearch:9200") //running inside container
                .connectedTo("localhost:9200") //running locally on machine
                .build();
    }
}
