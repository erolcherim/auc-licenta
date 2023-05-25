package com.unibuc.auclicenta.service;

import com.unibuc.auclicenta.data.Listing;
import com.unibuc.auclicenta.data.user.User;
import com.unibuc.auclicenta.exception.DuplicateEntityException;
import com.unibuc.auclicenta.exception.EntityNotFoundException;
import com.unibuc.auclicenta.exception.UserNotFoundException;
import com.unibuc.auclicenta.repository.ListingRepository;
import com.unibuc.auclicenta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {
    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Listing> getListingsForLoggedInUser() {
        String contextUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(contextUserEmail).orElseThrow(UserNotFoundException::new);
        List<Listing> favoriteListings = new ArrayList<>();
        for (String favorite :
                user.getFavorites()) {
            favoriteListings.add(listingRepository.findById(favorite).orElse(null));
            favoriteListings.remove(null);
        }
        return favoriteListings;
    }

    public String addListingToFavorites(String id) {
        String contextUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(contextUserEmail).orElseThrow(UserNotFoundException::new);
        List<String> favoriteListings = user.getFavorites();
        Listing listing = listingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!favoriteListings.contains(listing.getId())) {
            favoriteListings.add(listing.getId());
            user.setFavorites(favoriteListings);
            userRepository.save(user);
            return "Listing successfully added to favorites";
        } else {
            throw new DuplicateEntityException();
        }
    }

    public String removeListingFromFavorites(String id) {
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
        return "Listing successfully removed from favorites";
    }
}
