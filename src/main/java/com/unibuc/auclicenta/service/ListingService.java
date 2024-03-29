package com.unibuc.auclicenta.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.lang.NonNull;
import com.unibuc.auclicenta.controller.StringResponse;
import com.unibuc.auclicenta.controller.listing.*;
import com.unibuc.auclicenta.controller.user.UserResponse;
import com.unibuc.auclicenta.data.Listing;
import com.unibuc.auclicenta.exception.*;
import com.unibuc.auclicenta.repository.ListingRepository;
import org.jobrunr.jobs.annotations.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ListingService {
    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private ObjectMapper objectMapper;


    public Listing getListingById(String id) {
        return listingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public SearchResponse getListingByNameWithNumber(SearchRequest request) {
        // 0 = ASC, * = DESC
        Sort.Direction direction = request.getSortOrder() == 0 ? Sort.Direction.ASC : Sort.Direction.DESC;
        Long noResults = listingRepository.findByNameAndIsActive(request.getName(), 1, PageRequest.of(request.getPage(), request.getPageSize()).withSort(direction, request.getSortBy())).getTotalElements();
        List<Listing> l = listingRepository.findByNameAndIsActive(request.getName(), 1, PageRequest.of(request.getPage(), request.getPageSize()).withSort(direction, request.getSortBy())).toList();
        return SearchResponse.builder().noResults(noResults).listings(l).build();
    }

    public SearchResponse getListingMultiMatch(@NonNull MultiMatchSearchRequest request) {
        List<Listing> listings = listingRepository.findByNameNearPrice(request.getName(), request.getCurrentPrice(), request.getCurrentPrice() / 4, PageRequest.of(request.getPage(), request.getPageSize())).toList();
        Long noResults = listingRepository.findByNameNearPrice(request.getName(), request.getCurrentPrice(), request.getCurrentPrice() / 4, PageRequest.of(request.getPage(), request.getPageSize())).getTotalElements();
        return SearchResponse.builder().noResults(noResults).listings(listings).build();
    }

    public SearchResponse getLatestListings(SearchRequest request) {
        List<Listing> listings = listingRepository.findByIsActiveOrderByCreatedDateDesc(1, PageRequest.of(request.getPage(),
                request.getPageSize()).withSort(Sort.Direction.DESC, "createdDate")).toList();
        Long noResults = listingRepository.findByIsActiveOrderByCreatedDateDesc(1,
                PageRequest.of(request.getPage(), request.getPageSize()).withSort(Sort.Direction.DESC, "createdDate")).getTotalElements();
        return SearchResponse.builder().noResults(noResults).listings(listings).build();
    }

    public SearchResponse getListingsForUser(int page, int pageSize) {
        String userId = userService.getUserIdByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Listing> listings = listingRepository.findByUserId(userId, PageRequest.of(page, pageSize).withSort(Sort.Direction.DESC, "createdDate")).toList();
        Long noListings = listingRepository.findByUserId(userId, PageRequest.of(page, pageSize).withSort(Sort.Direction.DESC, "createdDate")).getTotalElements();
        return SearchResponse.builder().listings(listings).noResults(noListings).build();
    }

    public ListingResponse createListing(String request, MultipartFile image) throws JsonProcessingException {
        ListingRequest listingRequest = objectMapper.readValue(request, ListingRequest.class);
        if (listingRequest.getStartingPrice() > 0) {
            Listing listing = Listing.builder()
                    .userId(userService.getUserIdByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                    .name(listingRequest.getName())
                    .description(listingRequest.getDescription())
                    .startingPrice(listingRequest.getStartingPrice())
                    .currentPrice(listingRequest.getStartingPrice())
                    .bids(new ArrayList<>())
                    .isActive(0)
                    .build();

            listingRepository.save(listing);

            if (image != null) {
                try {
                    storageService.saveImage(image, listing.getId() + ".jpg");
                    listing.setHasImage(true);
                    listingRepository.save(listing);
                } catch (IOException e) {
                    //TODO saving image exception
                }
            }


            return ListingResponse.builder()
                    .id(listing.getId())
                    .name(listing.getName())
                    .description(listing.getDescription())
                    .startingPrice(listing.getStartingPrice())
                    .hasImage(listing.isHasImage())
                    .build();
        } else {
            throw new InvalidStartingPriceException();
        }
    }

    public StringResponse updateListing(String request, MultipartFile image, String id) throws JsonProcessingException {
        ListingRequest listingRequest = objectMapper.readValue(request, ListingRequest.class);
        Listing listingToUpdate = listingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        String currentUserId = userService.getUserIdByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!currentUserId.equals(listingToUpdate.getUserId())){
            throw new CanOnlyDeleteOwnListingException();
        }
        if (listingToUpdate.getIsActive() == 0) {
            String updatedName = listingRequest.getName();
            listingToUpdate.setName(updatedName);
            listingToUpdate.setDescription(listingRequest.getDescription());
            int updatedPrice = listingRequest.getStartingPrice();
            listingToUpdate.setStartingPrice(updatedPrice);
            listingToUpdate.setCurrentPrice(updatedPrice);
            listingToUpdate.setCreatedDate(new Date()); //Whenever a user modifies a listing during the pre-activation period the timer resets
            listingRepository.save(listingToUpdate);

            if (image != null) {
                try {
                    storageService.saveImage(image, listingToUpdate.getId() + ".jpg");
                    listingToUpdate.setHasImage(true);
                    listingRepository.save(listingToUpdate);
                } catch (IOException e) {
                    //TODO saving image exception
                }
            }

            return new StringResponse("Listing Updated Successfully");
        } else {
            throw new ListingIsActiveException();
        }
    }

    public StringResponse deleteListing(String id) {
        Listing listingToDelete = listingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<Listing.Bid> bidsProcessed = listingToDelete.getBids().stream()
                .filter(b -> userService.userExists(b.getBidderId()))
                .collect(Collectors.toList());

        if (bidsProcessed.size() != 0) {
            Listing.Bid highestBidder = bidsProcessed.get(bidsProcessed.size() - 1);
            bidsProcessed.remove(highestBidder);
            listingToDelete.setCurrentPrice(highestBidder.getUpdatedPrice());

            bidsProcessed.forEach(bi -> userService.refundBid(bi.getUpdatedPrice(), bi.getBidderId()));
        }
        listingRepository.delete(listingToDelete);
        return new StringResponse("Listing deleted successfully");
    }

    public StringResponse bidOnListing(BidRequest bidRequest, String id) {
        var listing = listingRepository.findByIdAndIsActive(id, 1).orElseThrow(EntityNotFoundException::new);
        int updatedPrice = bidRequest.getUpdatedPrice();

        String loggedInUserId = userService.getUserIdByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        UserResponse loggedInUser = userService.getUserById(loggedInUserId);

        if (loggedInUserId.equals(listing.getUserId())) {
            throw new CannotBidOnOwnListingException();
        }
        Listing.Bid bid = Listing.Bid.builder()
                .bidderId(loggedInUserId)
                .bidderName(loggedInUser.getFirstName() + " " + loggedInUser.getLastName().substring(0,1))
                .updatedPrice(bidRequest.getUpdatedPrice())
                .createdDate(new Date())
                .build();
        List<Listing.Bid> bids = listing.getBids();

        if (updatedPrice + 1 > Math.ceil(0.1 * listing.getStartingPrice()) + listing.getCurrentPrice()) {
            int accountBalance = userService.modifyBalance(bidRequest.getUpdatedPrice(), loggedInUserId);
            bids.add(bid);
            listing.setBids(bids);
            listing.setCurrentPrice(updatedPrice);
            listingRepository.save(listing);
//            favoriteService.addListingToFavorites(listing.getId()); //TODO 409 duplicate
            return new StringResponse("Bid successful, new price: " + listing.getCurrentPrice() + ". Current account balance: " + accountBalance);
        } else {
            throw new InvalidBidAmountException();
        }
    }

    /**
     * background tasks
     */
    @Job(name = "activateListingsRecurrent")
    public void activateListing() {
        List<Listing> inactiveListings = listingRepository.findByIsActive(0);

        inactiveListings.stream()
                .filter(x -> x.getCreatedDate().compareTo(new Date(System.currentTimeMillis() - 60 * 1000)) < 0) //Activate listings after 1 minutes TODO modify on release
                .forEach(x -> {
                    x.setIsActive(1);
                    x.setActivatedDate(new Date());
                    x.setExpirationDate(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)); // currently 30 days 60 * 10000
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
                    //remove users from list if they don't exist
                    List<Listing.Bid> bidsProcessed = x.getBids().stream()
                            .filter(b -> userService.userExists(b.getBidderId()))
                            .collect(Collectors.toList());

                    if (bidsProcessed.size() != 0) {
                        Listing.Bid highestBidder = bidsProcessed.get(bidsProcessed.size() - 1);
                        bidsProcessed.remove(highestBidder);
                        x.setCurrentPrice(highestBidder.getUpdatedPrice());

                        userService.addListingToWonListings(x.getId(), highestBidder.getBidderId());
                        userService.refundBid(highestBidder.getUpdatedPrice(), x.getUserId());
                        bidsProcessed.forEach(bi -> userService.refundBid(bi.getUpdatedPrice(), bi.getBidderId()));
                    }
                    listingRepository.save(x);
                });
    }

    public StringResponse deleteOwnListing(String id) {
        String loggedInUserId = userService.getUserIdByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Listing listingToDelete = listingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (listingToDelete.getIsActive() == 0 && listingToDelete.getUserId().equals(loggedInUserId)) {
            listingRepository.delete(listingToDelete);
        } else if(!listingToDelete.getUserId().equals(loggedInUserId)) {
            throw new CanOnlyDeleteOwnListingException();
        } else {
            throw new ListingIsActiveException();
        }
        return new StringResponse("Listing deleted successfully");
    }
}
