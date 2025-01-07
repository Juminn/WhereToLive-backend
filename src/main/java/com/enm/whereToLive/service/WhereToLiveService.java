package com.enm.whereToLive.service;

import com.enm.whereToLive.dto.OpportunityRequestDTO;
import com.enm.whereToLive.dto.OpportunityRequestDTO2;
import com.enm.whereToLive.dto.OpportunityResponseDTO;
import com.enm.whereToLive.dto.OpportunityResponseDTO2;
import com.enm.whereToLive.exception.ClusterNotFoundException;

public interface WhereToLiveService {

    OpportunityResponseDTO getPlaceOpportunity(OpportunityRequestDTO opportunityRequestDTO) throws Exception, ClusterNotFoundException;

    OpportunityResponseDTO2 getPlaceOpportunity2(OpportunityRequestDTO2 opportunityRequestDTO2);
}