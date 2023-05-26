package com.unibuc.auclicenta.service;

import com.mongodb.lang.NonNull;
import com.unibuc.auclicenta.controller.listing.*;
import com.unibuc.auclicenta.data.Listing;
import com.unibuc.auclicenta.exception.*;
import com.unibuc.auclicenta.repository.ListingRepository;
import org.jobrunr.jobs.annotations.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public ListingResponse createListing(ListingRequest listingRequest) {
        if (listingRequest.getStartingPrice() > 0) {
            var listing = Listing.builder()
                    .userId(userService.getUserIdByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                    .name(listingRequest.getName())
                    .startingPrice(listingRequest.getStartingPrice())
                    .currentPrice(listingRequest.getStartingPrice())
                    .bids(new ArrayList<>())
                    .isActive(0)
                    .build();

            listingRepository.save(listing);

            return ListingResponse.builder()
                    .id(listing.getId())
                    .name(listing.getName())
                    .startingPrice(listing.getStartingPrice())
                    .build();
        } else {
            throw new InvalidStartingPriceException();
        }
    }

    public String bidOnListing(BidRequest bidRequest, String id) {
        var listing = listingRepository.findByIdAndIsActive(id, 1).orElseThrow(EntityNotFoundException::new);
        int updatedPrice = bidRequest.getUpdatedPrice();

        String loggedInUserId = userService.getUserIdByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        Listing.Bid bid = Listing.Bid.builder()
                .bidderId(loggedInUserId)
                .updatedPrice(bidRequest.getUpdatedPrice())
                .createdDate(new Date())
                .build();
        List<Listing.Bid> bids = listing.getBids();
        //TODO bug se retrag currentPrice, nu bid amount din cont
        //TODO bug se retrag de mai multe ori desi da eroare?
        // if (4 > 1 + 4)
        if (updatedPrice + 1 > Math.ceil(0.1 * listing.getStartingPrice()) + listing.getCurrentPrice()) {
            int accountBalance = userService.modifyBalance(bidRequest.getUpdatedPrice(), loggedInUserId);
            bids.add(bid);
            listing.setBids(bids);
            listing.setCurrentPrice(updatedPrice);
            listingRepository.save(listing);
            return "Bid successful, new price: " + listing.getCurrentPrice() + ". Current account balance: " + accountBalance;
        } else {
            throw new InvalidBidAmountException();
        }
    }

    public Page<Listing> getListingByName(SearchRequest request) {
        // 0 = ASC, * = DESC
        Sort.Direction direction = request.getSortOrder() == 0 ? Sort.Direction.ASC : Sort.Direction.DESC;
        return listingRepository.findByName(request.getName(), PageRequest.of(request.getPage(), request.getPageSize()).withSort(direction, request.getSortBy()));
    }

    public Page<Listing> getListingMultiMatch(@NonNull MultiMatchSearchRequest request) {
        return listingRepository.findByNameNearPrice(request.getName(), request.getCurrentPrice(), PageRequest.of(request.getPage(), request.getPageSize()));
    }

    public Listing getListingById(String id) {
        return listingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Job(name = "activateListingsRecurrent")
    public void activateListing() {
        List<Listing> inactiveListings = listingRepository.findByIsActive(0);

        inactiveListings.stream()
                .filter(x -> x.getCreatedDate().compareTo(new Date(System.currentTimeMillis() - 60 * 1000)) < 0) //Activate listings after 1 minutes TODO modify on release
                .forEach(x -> {
                    x.setIsActive(1);
                    x.setActivatedDate(new Date());
                    x.setExpirationDate(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)); //60 * 1000
                    listingRepository.save(x);
                });
    }

    @Job(name = "deactivateExpiredListingsRecurrent")
    public void deactivateListing() {
        List<Listing> unexpiredListings = listingRepository.findByIsActive(1);

        unexpiredListings.stream()
                .filter(x -> x.getExpirationDate().compareTo(new Date(System.currentTimeMillis())) < 0)
                .forEach(x -> {
                    x.setIsActive(2);
                    for (Listing.Bid b : x.getBids()) {
                        try {
                            userService.refundBid(b.getUpdatedPrice(), b.getBidderId());
                        } catch (UserNotFoundException e) {
                            continue;
                        }
                    }
                    listingRepository.save(x);
                });
    }

    public String deleteListing(String id) {
        Listing listingToDelete = listingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (listingToDelete.getIsActive() == 0) {
            listingRepository.delete(listingToDelete);
        } else {
            throw new ListingIsActiveException();
        }
        return "Listing deleted successfully";
    }

    public ListingRequest updateListing(ListingRequest request, String id) {
        Listing listingToUpdate = listingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (listingToUpdate.getIsActive() == 0) {
            String updatedName = request.getName();
            listingToUpdate.setName(updatedName);
            int updatedPrice = request.getStartingPrice();
            listingToUpdate.setStartingPrice(updatedPrice);
            listingToUpdate.setCurrentPrice(updatedPrice);
            listingToUpdate.setCreatedDate(new Date()); //Whenever a user modifies a listing during the pre-activation period the timer resets
            listingRepository.save(listingToUpdate);
            return ListingRequest.builder()
                    .name(updatedName)
                    .startingPrice(updatedPrice)
                    .build();
        } else {
            throw new ListingIsActiveException();
        }
    }
}
