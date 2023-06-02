package com.unibuc.auclicenta.controller.listing;

import com.unibuc.auclicenta.controller.StringResponse;
import com.unibuc.auclicenta.data.Listing;
import com.unibuc.auclicenta.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<StringResponse> bidOnListing(@RequestBody BidRequest bidRequest, @PathVariable String id) {
        return ResponseEntity.ok(listingService.bidOnListing(bidRequest, id));
    }

    @GetMapping("/search/{id}")
    @ResponseBody
    public ResponseEntity<Listing> getListingById(@PathVariable String id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }

    @GetMapping("/search/current-user")
    @ResponseBody
    public ResponseEntity<SearchResponse> getListingsForCurrentUser(@RequestParam int page, @RequestParam int pageSize){
        return ResponseEntity.ok(listingService.getListingsForUser(page, pageSize));
    }

    @PostMapping("/search/latest")
    public ResponseEntity<SearchResponse> getLatestListings(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(listingService.getLatestListings(request));
    }

    @PostMapping("/search/multi-search")
    @ResponseBody
    public ResponseEntity<SearchResponse> getSearchHitScoresForNameQuery(@RequestBody MultiMatchSearchRequest request) {
        return ResponseEntity.ok(listingService.getListingMultiMatch(request));
    }

    @PostMapping("/search/name-search")
    @ResponseBody
    public ResponseEntity<SearchResponse> getSearchResultsByName(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(listingService.getListingByNameWithNumber(request));
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
