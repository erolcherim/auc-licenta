package com.unibuc.auclicenta.controller.favorites;

import com.unibuc.auclicenta.data.Listing;
import com.unibuc.auclicenta.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/")
    public ResponseEntity<List<Listing>> getAllListings() {
        return ResponseEntity.ok(favoriteService.getListingsForLoggedInUser());
    }

    @PutMapping("/add/{id}")
    public ResponseEntity<String> addListingToFavorites(@PathVariable String id) {
        return ResponseEntity.ok(favoriteService.addListingToFavorites(id));
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<String> removeListingFromFavorites(@PathVariable String id) {
        return ResponseEntity.ok(favoriteService.removeListingFromFavorites(id));
    }
}
