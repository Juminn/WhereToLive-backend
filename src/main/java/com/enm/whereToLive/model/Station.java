package com.enm.whereToLive.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Station {
    private Integer id;
    private String destination;
    private String line;
    private String name;
    private Double latitude;
    private Double longitude;
    private Double montlyRent;
    private Double montlyGoingOpportunity;
    private Double montlyTotalOpportunity;
    private Integer goingWorkMinute;
    private String pros;
    private String cons;
}
