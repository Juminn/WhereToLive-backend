package com.enm.whereToLive.data;

import com.enm.whereToLive.data.cluster.LivingOpportunityMySQL;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class opportunityResponseDTO {
    Destination destination;
    List<LivingOpportunityMySQL> livingOpportunities;
}
