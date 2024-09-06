package com.enm.whereToLive.Service.impl;

import com.enm.whereToLive.Data.Station;
import com.enm.whereToLive.Service.StationService;
import com.enm.whereToLive.Service.WhereToLiveService;
import com.enm.whereToLive.Service.dabang.DabangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WhereToLiveServiceImpl implements WhereToLiveService {

    private StationService stationService;
    private DabangService dabangService;
    private com.enm.whereToLive.Service.whenToGo.WhenToGoService whenToGoService;

    @Autowired
    public WhereToLiveServiceImpl(StationService stationService, DabangService dabangService, com.enm.whereToLive.Service.whenToGo.WhenToGoService whenToGoService) {
        this.stationService = stationService;
        this.dabangService = dabangService;
        this.whenToGoService = whenToGoService;
    }


    @Override
    public ArrayList<Station> getStationsOpportunity(String destination) throws Exception {
        ArrayList<Station> stations= stationService.getStationsByDeparture(destination);

        return stations;
    }
}
