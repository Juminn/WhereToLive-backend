package com.enm.whereToLive.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivingOpportunityEntityDynamoID {
    @DynamoDBHashKey(attributeName = "destination")
    private String destination;

    @DynamoDBRangeKey(attributeName = "station_id")
    private Integer stationID;
}