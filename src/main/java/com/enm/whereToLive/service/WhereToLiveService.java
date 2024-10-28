package com.enm.whereToLive.service;

import com.enm.whereToLive.data.opportunityResponseDTO;
import com.enm.whereToLive.data.opportunityResponseDTO2;

public interface WhereToLiveService {

    opportunityResponseDTO getPlaceOpportunity(double latitude, Double longitude, int workDays) throws Exception;

    opportunityResponseDTO2 getPlaceOpportunity2(String name, int workDays);
}