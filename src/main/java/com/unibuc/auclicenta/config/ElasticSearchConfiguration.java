package com.unibuc.auclicenta.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@Configuration
public class ElasticSearchConfiguration {
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials()

    @Bean
    public RestClient restClient(){
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();
        return restClient;
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport(){
        return new RestClientTransport(
                restClient(), new JacksonJsonpMapper());
    }
    @Bean
    public ElasticsearchClient elasticsearchClient(){
        ElasticsearchClient elasticsearchClient = new ElasticsearchClient(elasticsearchTransport());
        return elasticsearchClient;
    }
}
