package com.unibuc.auclicenta.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "test")
public class TestEntity {
    @Id
    private String id;
    private String t;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public TestEntity(String id, String t) {
        this.id = id;
        this.t = t;
    }
}
