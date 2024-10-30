package com.enm.whereToLive.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "living_opportunity")
public class LivingOpportunityEntityMySQL {

    @EmbeddedId
    private LivingOpportunityEntityMySQLID id;
//    private String destination;
//
//    private Integer stationID;

    private String stationName;
    private String line;
    private Double latitude;
    private Double longitude;
    private Integer rentCost;
    private Integer commuteCost;
    private Integer totalOpportunityCost;
    private Integer commuteTime;

    private String pros;
    private String cons;
}