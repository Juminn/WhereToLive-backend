package com.enm.whereToLive.data.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivingOpportunityDynamoID {
    @DynamoDBHashKey(attributeName = "destination")
    private String destination;

    @DynamoDBRangeKey(attributeName = "station_id")
    private Integer stationID;
}