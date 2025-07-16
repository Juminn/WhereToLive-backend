package com.enm.whereToLive.service;

import com.enm.whereToLive.dto.request.OpportunityRequestDTO;
import com.enm.whereToLive.dto.request.OpportunityRequestDTO2;
import com.enm.whereToLive.dto.response.OpportunityResponseDTO;
import com.enm.whereToLive.dto.response.OpportunityResponseDTO2;

public interface WhereToLiveService {

    OpportunityResponseDTO getPlaceOpportunity(OpportunityRequestDTO opportunityRequestDTO);

    OpportunityResponseDTO2 getPlaceOpportunity2(OpportunityRequestDTO2 opportunityRequestDTO2);

    boolean isVaildWorkdays(int workdays);
}