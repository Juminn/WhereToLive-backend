package com.enm.whereToLive.service;

import com.enm.whereToLive.data.Destination;
import com.enm.whereToLive.data.Station;

import java.util.ArrayList;

public interface BatchService {

    ArrayList<Station> batchMakeOpportunityDemo(Destination destination) throws Exception;
    ArrayList<Station> batchMakeOpportunity(Destination destination) throws Exception;
    public String getStationsRental(ArrayList<Station> stationList) throws Exception;
}