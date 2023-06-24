package com.unibuc.auclicenta.controller.listing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingRequest {
    private String name;
    private String description;
    private Integer startingPrice;
    private MultipartFile image;
}
