package com.enm.whereToLive.service;

import com.enm.whereToLive.data.entity.LivingOpportunity;
import com.enm.whereToLive.data.opportunityResponseDTO;

import java.util.List;

public interface WhereToLiveService {

    opportunityResponseDTO getPlaceOpportunity(String destination, int workDays) throws Exception;
}