package com.enm.whereToLive.dto;

import com.enm.whereToLive.entity.LivingOpportunityEntityMySQL;
import com.enm.whereToLive.model.Destination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class opportunityResponseDTO {
    Destination destination;
    List<LivingOpportunityEntityMySQL> livingOpportunities;
}
