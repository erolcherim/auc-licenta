package com.unibuc.auclicenta.controller.listing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingResponse {
    private String id;
    private String name;
    private Integer startingPrice;
}
