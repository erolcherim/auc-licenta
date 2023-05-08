package com.unibuc.auclicenta.service;

import com.unibuc.auclicenta.controller.listing.ListingRequest;
import com.unibuc.auclicenta.data.listing.Listing;
import com.unibuc.auclicenta.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

@Service
public class ListingService {
    @Autowired
    private ListingRepository listingRepository;

    public ListingRequest saveListing(ListingRequest listingRequest) {
        var listing = Listing.builder()
                .name(listingRequest.getName())
                .price(listingRequest.getPrice())
                .build();
        listingRepository.save(listing);
        return ListingRequest.builder()
                .name(listingRequest.getName())
                .price(listingRequest.getPrice())
                .build();
    }

    public SearchHits<Listing> getListingByName(String name) {
        return listingRepository.findByName(name);
    }

}
