package com.enm.whereToLive.Service;

import com.enm.whereToLive.Data.Station;

import java.util.ArrayList;

public interface WhereToLiveService {

    ArrayList<Station> getStationsOpportunity(String destination) throws Exception;

}