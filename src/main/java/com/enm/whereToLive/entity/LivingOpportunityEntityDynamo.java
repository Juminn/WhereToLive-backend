package com.enm.whereToLive.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;
import org.springframework.data.annotation.Id;

@ToString
@AllArgsConstructor
@DynamoDBTable(tableName = "LivingOpportunity")
public class LivingOpportunityEntityDynamo {

    @Id
    private LivingOpportunityEntityDynamoID id; // 복합 키를 위한 ID 클래스

    public LivingOpportunityEntityDynamo() {
        this.id = new LivingOpportunityEntityDynamoID();
    }

    @DynamoDBHashKey(attributeName = "destination")
    public String getDestination() {
        return this.id.getDestination();
    }

    public void setDestination(String destination) {
        this.id.setDestination(destination);
    }

    @DynamoDBRangeKey(attributeName = "station_id")
    public Integer getStationID() {
        return this.id.getStationID();
    }

    public void setStationID(Integer stationID) {
        this.id.setStationID(stationID);
    }

    // Lombok을 사용한 필드별 getter와 setter
    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "station_name")
    private String stationName;

    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "line")
    private String line;

    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "latitude")
    private Double latitude;

    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "longitude")
    private Double longitude;

    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "rent_cost")
    private Integer rentCost;

    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "commute_cost")
    private Integer commuteCost;

    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "total_opportunity_cost")
    private Integer totalOpportunityCost;

    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "commute_time")
    private Integer commuteTime;

    @Getter
    @Setter
    private String pros;

    @Getter
    @Setter
    private String cons;

}