package com.enm.whereToLive.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LivingOpportunityEntityMySQLID implements Serializable {

    private String destination;
    private Integer stationId;

}
