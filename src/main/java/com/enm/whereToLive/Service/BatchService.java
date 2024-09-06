package com.enm.whereToLive.Service;

import com.enm.whereToLive.Data.Destination;
import com.enm.whereToLive.Data.Station;

import java.util.ArrayList;

public interface BatchService {

    ArrayList<Station> batchMakeOpportunity(Destination destination) throws Exception;

}