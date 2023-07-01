package com.unibuc.auclicenta.controller.favorites;

import com.unibuc.auclicenta.controller.StringResponse;
import com.unibuc.auclicenta.controller.listing.SearchResponse;
import com.unibuc.auclicenta.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/")
    public ResponseEntity<SearchResponse> getFavoritesCurrent(@RequestParam int page, @RequestParam int pageSize) {
        return ResponseEntity.ok(favoriteService.getFavoritesForLoggedInUser(page, pageSize));
    }

    @GetMapping("/won")
    public ResponseEntity<SearchResponse> getWonCurrent(@RequestParam int page, @RequestParam int pageSize) {
        return ResponseEntity.ok(favoriteService.getWonListingsForLoggedInUser(page, pageSize));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<String>> getFavoritesIdsCurrent() {
        return ResponseEntity.ok(favoriteService.getFavoritesIdsCurrent());
    }

    @PutMapping("/add/{id}")
    public ResponseEntity<StringResponse> addListingToFavorites(@PathVariable String id) {
        return ResponseEntity.ok(favoriteService.addListingToFavorites(id));
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<StringResponse> removeListingFromFavorites(@PathVariable String id) {
        return ResponseEntity.ok(favoriteService.removeListingFromFavorites(id));
    }
}
