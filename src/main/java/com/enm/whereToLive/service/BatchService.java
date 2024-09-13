package com.enm.whereToLive.service;

import com.enm.whereToLive.data.Destination;
import com.enm.whereToLive.data.Station;

import java.util.ArrayList;

public interface BatchService {

    ArrayList<Station> batchMakeOpportunity(Destination destination) throws Exception;

}