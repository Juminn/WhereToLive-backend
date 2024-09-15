package com.enm.whereToLive.data.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LivingOpportunityId {
    @DynamoDBHashKey(attributeName = "test")
    private String test;

    @DynamoDBRangeKey(attributeName = "tests")
    private String tests;
}