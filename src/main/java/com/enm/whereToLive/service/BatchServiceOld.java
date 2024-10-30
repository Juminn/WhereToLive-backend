package com.enm.whereToLive.service;

import com.enm.whereToLive.model.Destination;
import com.enm.whereToLive.model.Station;

import java.util.ArrayList;

public interface BatchServiceOld {

    ArrayList<Station> batchMakeOpportunityDemo(Destination destination) throws Exception;
    ArrayList<Station> batchMakeOpportunity(Destination destination) throws Exception;
    public String getStationsRental(ArrayList<Station> stationList) throws Exception;
}