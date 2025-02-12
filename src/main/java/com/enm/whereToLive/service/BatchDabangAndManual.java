package com.enm.whereToLive.service;

import com.enm.whereToLive.model.Destination;
import com.enm.whereToLive.model.Station;

import java.util.ArrayList;

public interface BatchDabangAndManual {
    ArrayList<Station> batchMakeOpportunity(Destination destination) throws Exception;
    public String getStationsRental(ArrayList<Station> stationList) throws Exception;
}