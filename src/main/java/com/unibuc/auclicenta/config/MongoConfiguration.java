package com.unibuc.auclicenta.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.lang.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableMongoRepositories("com.unibuc.auclicenta.repository")
@EnableMongoAuditing
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    @Value("${mongodb.connection.url}")
    private String connectionURL;

    @NonNull
    @Override
    protected String getDatabaseName() {
        return "defaultdb";
    }

    @NonNull
    @Override
    public MongoClient mongoClient() {
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionURL))
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @NonNull
    @Override
    public Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.unibuc.auc-licenta.data");
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}
