package com.unibuc.auclicenta.data.listing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(indexName = "listings")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Listing implements Persistable<String>, Serializable {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String userId;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Integer)
    private Integer startingPrice;
    @Field(type = FieldType.Integer)
    private Integer currentPrice;
    @Field(type = FieldType.Object)
    private List<Bid> bids;
    @CreatedDate
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Date createdDate;

    @Override
    public boolean isNew() {
        return (this.id == null);
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bid {
        @Field(type = FieldType.Text)
        private String bidderId;
        @Field(type = FieldType.Integer)
        private Integer updatedPrice;
        @Field(type = FieldType.Date, format = DateFormat.date_time)
        private Date createdDate;
    }
}
