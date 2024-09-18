package com.enm.whereToLive.service;

import com.enm.whereToLive.data.Station;

import java.util.ArrayList;

public interface WhereToLiveService {

    ArrayList<Station> getStationsOpportunity(String destination) throws Exception;
}