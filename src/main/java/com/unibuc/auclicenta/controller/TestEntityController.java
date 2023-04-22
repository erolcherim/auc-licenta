package com.unibuc.auclicenta.controller;

import com.unibuc.auclicenta.data.TestEntity;
import com.unibuc.auclicenta.data.TestEntityQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Controller
public class TestEntityController {
    @Autowired
    TestEntityQuery testEntityQuery;
    @PostMapping("/test")
    private void saveEntity(@RequestBody TestEntity testEntity) throws IOException {
        testEntityQuery.createDocument(testEntity);
    }
}
