package com.enm.whereToLive.service;

import com.enm.whereToLive.data.entity.LivingOpportunity;

import java.util.List;

public interface WhereToLiveService {

    List<LivingOpportunity> getPlaceOpportunity(String destination, int workDays) throws Exception;
}