package com.unibuc.auclicenta.controller.listing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unibuc.auclicenta.controller.StringResponse;
import com.unibuc.auclicenta.data.Listing;
import com.unibuc.auclicenta.service.ListingService;
import com.unibuc.auclicenta.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/api/v1/listing")
public class ListingController {
    @Autowired
    private ListingService listingService;
    @Autowired
    private StorageService storageService;

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ListingResponse> saveListing(@RequestParam("model") String listingRequest, @RequestParam(value = "file", required = false) MultipartFile image) throws JsonProcessingException {
        return ResponseEntity.ok(listingService.createListing(listingRequest, image));
    }

    @PostMapping("/bid/{id}")
    @ResponseBody
    public ResponseEntity<StringResponse> bidOnListing(@RequestBody BidRequest bidRequest, @PathVariable String id) {
        return ResponseEntity.ok(listingService.bidOnListing(bidRequest, id));
    }

    @GetMapping(path = "image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody ResponseEntity<byte[]> getImageByListingId(@PathVariable String id) {
        return ResponseEntity.ok(storageService.getImage(id));
    }

    @GetMapping("/search/{id}")
    @ResponseBody
    public ResponseEntity<Listing> getListingById(@PathVariable String id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }

    @GetMapping("/search/current-user")
    @ResponseBody
    public ResponseEntity<SearchResponse> getListingsForCurrentUser(@RequestParam int page, @RequestParam int pageSize) {
        return ResponseEntity.ok(listingService.getListingsForUser(page, pageSize));
    }

    @PostMapping("/search/latest")
    public ResponseEntity<SearchResponse> getLatestListings(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(listingService.getLatestListings(request));
    }

    @PostMapping("/search/multi-search")
    @ResponseBody
    public ResponseEntity<SearchResponse> getMultiSearchResults(@RequestBody MultiMatchSearchRequest request) {
        return ResponseEntity.ok(listingService.getListingMultiMatch(request));
    }

    @PostMapping("/search/name-search")
    @ResponseBody
    public ResponseEntity<SearchResponse> getSearchResultsByName(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(listingService.getListingByNameWithNumber(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StringResponse> updateListingById(@RequestParam("model") String listingRequest, @RequestParam(value = "file", required = false) MultipartFile image, @PathVariable String id) throws JsonProcessingException {
        return ResponseEntity.ok(listingService.updateListing(listingRequest, image, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StringResponse> deleteOwnListingById(@PathVariable String id){
        return ResponseEntity.ok(listingService.deleteOwnListing(id));
    }

    @DeleteMapping("/admin/{id}")
    @ResponseBody
    public ResponseEntity<StringResponse> deleteListing(@PathVariable String id) {
        return ResponseEntity.ok(listingService.deleteListing(id));
    }
}
