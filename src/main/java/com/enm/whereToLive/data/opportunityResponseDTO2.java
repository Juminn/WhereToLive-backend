package com.enm.whereToLive.data;

import com.enm.whereToLive.data.entity.LivingOpportunityDynamo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class opportunityResponseDTO2 {
    Destination destination;
    List<LivingOpportunityDynamo> livingOpportunities;
}
