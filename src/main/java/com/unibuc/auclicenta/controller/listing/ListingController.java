package com.unibuc.auclicenta.controller.listing;

import com.unibuc.auclicenta.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/api/v1/listing")
public class ListingController {
    @Autowired
    private ListingService listingService;

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ListingRequest> saveListing(@RequestBody ListingRequest listingRequest) {
        return ResponseEntity.ok(listingService.saveListing(listingRequest));
    }

    @GetMapping("/results/scores")
    @ResponseBody
    public List<Float> getSearchHitScoresForNameQuery(@RequestParam String name) {
        return listingService.getListingByName(name).stream().map(SearchHit::getScore).collect(Collectors.toList());
    }
}
