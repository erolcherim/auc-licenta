package com.unibuc.auclicenta.controller.listing;

import com.unibuc.auclicenta.data.Listing;
import com.unibuc.auclicenta.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api/v1/listing")
public class ListingController {
    @Autowired
    private ListingService listingService;

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ListingResponse> saveListing(@RequestBody ListingRequest listingRequest) {
        return ResponseEntity.ok(listingService.createListing(listingRequest));
    }

    @PostMapping("/bid/{id}")
    @ResponseBody
    public ResponseEntity<String> bidOnListing(@RequestBody BidRequest bidRequest, @PathVariable String id) {
        return ResponseEntity.ok(listingService.bidOnListing(bidRequest, id));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Listing> getListingById(@PathVariable String id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }

    @GetMapping("/multi-search")
    @ResponseBody
    public ResponseEntity<List<Listing>> getSearchHitScoresForNameQuery(@RequestBody MultiMatchSearchRequest request) {
        return ResponseEntity.ok(listingService.getListingMultiMatch(request).toList());
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<Listing>> getSearchHitScoresForNameQuery(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(listingService.getListingByName(request).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingRequest> updateListingById(@RequestBody ListingRequest request, @PathVariable String id) {
        return ResponseEntity.ok(listingService.updateListing(request, id));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteListing(@PathVariable String id) {
        return ResponseEntity.ok(listingService.deleteListing(id));
    }
}
