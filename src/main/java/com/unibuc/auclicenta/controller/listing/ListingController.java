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
    public ResponseEntity<ListingRequest> saveListing(@RequestBody ListingRequest listingRequest) {
        return ResponseEntity.ok(listingService.createListing(listingRequest));
    }

    @PutMapping("/bid/search")
    @ResponseBody
    public void bidOnListing(@RequestBody BidRequest bidRequest, @RequestParam String id) {
        listingService.bidOnListing(bidRequest, id);
    }

    @GetMapping("/results/search")
    @ResponseBody
    public List<Listing> getSearchHitScoresForNameQuery(@RequestBody MultiMatchSearchRequest request) {
        return listingService.getListingMultiMatch(request).toList();
    }
}
