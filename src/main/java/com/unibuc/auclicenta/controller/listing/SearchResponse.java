package com.unibuc.auclicenta.controller.listing;

import com.unibuc.auclicenta.data.Listing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    public Long noResults;
    public List<Listing> listings;
}
