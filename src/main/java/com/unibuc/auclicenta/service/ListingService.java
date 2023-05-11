package com.unibuc.auclicenta.service;

import com.unibuc.auclicenta.controller.listing.BidRequest;
import com.unibuc.auclicenta.controller.listing.ListingRequest;
import com.unibuc.auclicenta.data.listing.Listing;
import com.unibuc.auclicenta.exception.EntityNotFoundException;
import com.unibuc.auclicenta.exception.InvalidBidAmountException;
import com.unibuc.auclicenta.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ListingService {
    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private UserService userService;

    public ListingRequest createListing(ListingRequest listingRequest) {
        var listing = Listing.builder()
                .userId(userService.getUserIdByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                .name(listingRequest.getName())
                .startingPrice(listingRequest.getStartingPrice())
                .currentPrice(listingRequest.getStartingPrice())
                .bids(new ArrayList<>())
                .build();

        listingRepository.save(listing);

        return ListingRequest.builder()
                .name(listingRequest.getName())
                .startingPrice(listingRequest.getStartingPrice())
                .build();
    }

    public void bidOnListing(BidRequest bidRequest, String id) {
        var listing = listingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Integer updatedPrice = bidRequest.getUpdatedPrice();

        Listing.Bid bid = Listing.Bid.builder()
                .bidderId(userService.getUserIdByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                .updatedPrice(bidRequest.getUpdatedPrice())
                .createdDate(new Date())
                .build();
        List<Listing.Bid> bids = listing.getBids();

        if (updatedPrice + 1 >= 1.1 * listing.getStartingPrice()) {
            bids.add(bid);
            listing.setBids(bids);
            listing.setCurrentPrice(updatedPrice);
            listingRepository.save(listing);
        } else {
            throw new InvalidBidAmountException();
        }
    }

    public SearchHits<Listing> getListingByName(String name) {
        return listingRepository.findByName(name);
    }
}
