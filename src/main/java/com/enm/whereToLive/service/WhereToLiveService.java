package com.enm.whereToLive.service;

import com.enm.whereToLive.data.entity.LivingOpportunity;
import com.enm.whereToLive.data.opportunityResponseDTO;

import java.util.List;

public interface WhereToLiveService {

    opportunityResponseDTO getPlaceOpportunity(double latitude, Double longitude, int workDays) throws Exception;

    opportunityResponseDTO getPlaceOpportunity2(String name, int workDays);
}