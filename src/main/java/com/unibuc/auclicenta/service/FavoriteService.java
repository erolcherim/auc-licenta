package com.unibuc.auclicenta.service;

import com.unibuc.auclicenta.controller.StringResponse;
import com.unibuc.auclicenta.controller.listing.SearchResponse;
import com.unibuc.auclicenta.data.Listing;
import com.unibuc.auclicenta.data.user.User;
import com.unibuc.auclicenta.exception.DuplicateEntityException;
import com.unibuc.auclicenta.exception.EntityNotFoundException;
import com.unibuc.auclicenta.exception.UserNotFoundException;
import com.unibuc.auclicenta.repository.ListingRepository;
import com.unibuc.auclicenta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private UserRepository userRepository;

    public SearchResponse getFavoritesForLoggedInUser(int page, int pageSize) {
        String contextUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(contextUserEmail).orElseThrow(UserNotFoundException::new);
        List<Listing> favorites = listingRepository.findByIdIn(user.getFavorites().toArray(new String[0]), PageRequest.of(page, pageSize)).toList();
        Long noFavorites = listingRepository.findByIdIn(user.getFavorites().toArray(new String[0]), PageRequest.of(page, pageSize)).getTotalElements();
        return SearchResponse.builder().listings(favorites).noResults(noFavorites).build();
    }

    public SearchResponse getWonListingsForLoggedInUser(int page, int pageSize) {
        String contextUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(contextUserEmail).orElseThrow(UserNotFoundException::new);
        List<Listing> wonBids = listingRepository.findByIdIn(user.getWonBids().toArray(new String[0]), PageRequest.of(page, pageSize)).toList();
        Long noWonBids = listingRepository.findByIdIn(user.getWonBids().toArray(new String[0]), PageRequest.of(page, pageSize)).getTotalElements();
        return SearchResponse.builder().listings(wonBids).noResults(noWonBids).build();
    }

    public List<String> getFavoritesIdsCurrent(){
        String contextUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(contextUserEmail).orElseThrow(UserNotFoundException::new);
        return user.getFavorites();
    }

    public StringResponse addListingToFavorites(String id) {
        String contextUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(contextUserEmail).orElseThrow(UserNotFoundException::new);
        List<String> favoriteListings = user.getFavorites();
        Listing listing = listingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!favoriteListings.contains(listing.getId())) {
            favoriteListings.add(listing.getId());
            user.setFavorites(favoriteListings);
            userRepository.save(user);
            return new StringResponse("Listing successfully added to favorites");
        } else {
            throw new DuplicateEntityException();
        }
    }

    public StringResponse removeListingFromFavorites(String id) {
        String contextUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(contextUserEmail).orElseThrow(UserNotFoundException::new);
        List<String> favoriteListings = user.getFavorites();
        if (favoriteListings.contains(id)) {
            favoriteListings.remove(id);
            user.setFavorites(favoriteListings);
            userRepository.save(user);
        } else {
            throw new EntityNotFoundException();
        }
        return new StringResponse("Listing successfully removed from favorites");
    }
}
