package com.unibuc.auclicenta.controller.listing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultiMatchSearchRequest {
    private static final Integer DEFAULT_SIZE=25;
    private String name;
    private int currentPrice;
    private Integer page;
    private Integer pageSize;
    public Integer getPageSize(){
        return pageSize != 0 ? pageSize : DEFAULT_SIZE;
    }
}
