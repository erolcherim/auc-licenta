package com.unibuc.auclicenta.controller.favorites;

import com.unibuc.auclicenta.controller.listing.SearchResponse;
import com.unibuc.auclicenta.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/query")
    public ResponseEntity<SearchResponse> getAllListings(@RequestParam int page, @RequestParam int pageSize) {
        return ResponseEntity.ok(favoriteService.getListingsForLoggedInUser(page, pageSize));
    }

    @PutMapping("/add/{id}") //TODO: make it POST
    public ResponseEntity<String> addListingToFavorites(@PathVariable String id) {
        return ResponseEntity.ok(favoriteService.addListingToFavorites(id));
    }

    @PutMapping("/remove/{id}") //TODO: make it DELETE
    public ResponseEntity<String> removeListingFromFavorites(@PathVariable String id) {
        return ResponseEntity.ok(favoriteService.removeListingFromFavorites(id));
    }
}
