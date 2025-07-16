package com.enm.whereToLive.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LivingOpportunityDTO {
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