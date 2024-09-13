package com.enm.whereToLive.service.impl;

import com.enm.whereToLive.data.Station;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.WhereToLiveService;
import com.enm.whereToLive.service.dabang.DabangService;
import com.enm.whereToLive.service.whenToGo.WhenToGoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WhereToLiveServiceImpl implements WhereToLiveService {

    private StationService stationService;
    private DabangService dabangService;
    private WhenToGoService whenToGoService;
   // private

    @Autowired
    public WhereToLiveServiceImpl(StationService stationService, DabangService dabangService, com.enm.whereToLive.service.whenToGo.WhenToGoService whenToGoService) {
        this.stationService = stationService;
        this.dabangService = dabangService;
        this.whenToGoService = whenToGoService;
    }


    @Override
    public ArrayList<Station> getStationsOpportunity(String destination) throws Exception {
        ArrayList<Station> stations= stationService.getStationsByDeparture(destination);

        return stations;
    }

    @Override
    public void test() {

    }
}
