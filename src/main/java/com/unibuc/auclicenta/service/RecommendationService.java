package com.unibuc.auclicenta.service;

import com.unibuc.auclicenta.controller.listing.RecommendationRequest;
import com.unibuc.auclicenta.data.Listing;
import com.unibuc.auclicenta.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {
    @Autowired
    private ListingRepository listingRepository;

    public List<Listing> getListingRecommendations(RecommendationRequest recommendationRequest) {
        int noCategories = recommendationRequest.getSearchHits().size();
        Map<SearchHit<Listing>, Integer> totalSearchHits = new HashMap<>();

        for (int i = 1; i <= noCategories; i++) {
            List<SearchHit<Listing>> currentSearch;
            currentSearch = listingRepository.findByNameAndIsActive(recommendationRequest.getSearchHits().get(i - 1), 1);
            for (SearchHit<Listing> searchHit : currentSearch) {
                totalSearchHits.put(searchHit, i);
            }
        }

        List<Listing> recommendedResults = new ArrayList<>();
        knapsackWithOneItemPerCategory(recommendationRequest.getBudget(), totalSearchHits, noCategories, recommendedResults);
        return recommendedResults;
    }

    public void knapsackWithOneItemPerCategory(int capacity, Map<SearchHit<Listing>, Integer> items, int numCategories, List<Listing> selectedItems) {
        float[][] dp = new float[capacity + 1][numCategories + 1];
        int[][] selected = new int[capacity + 1][numCategories + 1];

        for (Map.Entry<SearchHit<Listing>, Integer> item : items.entrySet()) {
            for (int j = capacity; j >= item.getKey().getContent().getCurrentPrice(); j--) {
                for (int k = numCategories; k >= 1; k--) {
                    if (item.getValue() == k) {
                        float previousValue = dp[j][k];
                        float previousWeightValue = dp[j - item.getKey().getContent().getCurrentPrice()][k - 1];

                        float newValue = previousWeightValue + item.getKey().getScore();
                        if (newValue > previousValue) {
                            dp[j][k] = newValue;
                            selected[j][k] = item.getKey().getContent().getCurrentPrice();
                        } else {
                            dp[j][k] = previousValue;
                        }
                    }
                }
            }
        }

        int remainingCapacity = capacity;

        // Retrieve the selected items
        for (int k = numCategories; k >= 1; k--) {
            int selectedItemWeight = selected[remainingCapacity][k];
            for (Map.Entry<SearchHit<Listing>, Integer> item : items.entrySet()) {
                if (item.getValue() == k && item.getKey().getContent().getCurrentPrice() == selectedItemWeight) {
                    selectedItems.add(item.getKey().getContent());
                    remainingCapacity -= item.getKey().getContent().getCurrentPrice();
                    break;
                }
            }
        }
    }
}

