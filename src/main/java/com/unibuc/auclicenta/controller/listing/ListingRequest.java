package com.unibuc.auclicenta.controller.listing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingRequest {
    private String name;
    private String description;
    private Integer startingPrice;
}
