package com.enm.whereToLive.dto;

import com.enm.whereToLive.entity.LivingOpportunityEntityMySQL;
import com.enm.whereToLive.model.Destination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpportunityResponseDTO {
    Destination destination;
    List<LivingOpportunityEntityMySQL> livingOpportunities;
}
