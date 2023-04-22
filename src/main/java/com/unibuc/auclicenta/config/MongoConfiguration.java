package com.unibuc.auclicenta.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.Collection;
import java.util.Collections;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    @Value("${mongodb.connection.url}")
    private String connectionURL;

    @Override
    protected String getDatabaseName() {
        return "defaultdb";
    }

    @Override
    public MongoClient mongoClient() {
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionURL))
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public Collection getMappingBasePackages() {
        return Collections.singleton("com.unibuc.auc-licenta.data");
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}
