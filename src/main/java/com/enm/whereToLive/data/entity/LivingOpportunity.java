package com.enm.whereToLive.data.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "LivingOpportunity")
public class LivingOpportunity {

    @DynamoDBHashKey
    private String destination;
    @DynamoDBAttribute
    private String stationId;

}
