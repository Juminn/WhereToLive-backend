package com.enm.whereToLive.service.impl;

import com.enm.whereToLive.data.Station;
import com.enm.whereToLive.data.entity.LivingOpportunity;
import com.enm.whereToLive.data.entity.LivingOpportunityId;
import com.enm.whereToLive.data.repository.LivingOpportunityRepository;
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
    private LivingOpportunityRepository livingOpportunityRepository;

    @Autowired
    public WhereToLiveServiceImpl(StationService stationService, DabangService dabangService, com.enm.whereToLive.service.whenToGo.WhenToGoService whenToGoService, LivingOpportunityRepository livingOpportunityRepository) {
        this.stationService = stationService;
        this.dabangService = dabangService;
        this.whenToGoService = whenToGoService;
        this.livingOpportunityRepository = livingOpportunityRepository;
    }


    @Override
    public ArrayList<Station> getStationsOpportunity(String destination) throws Exception {
        ArrayList<Station> stations= stationService.getStationsByDeparture(destination);

        return stations;
    }

    @Override
    public void test() {
        LivingOpportunity livingOpportunity = new LivingOpportunity(new LivingOpportunityId("카카오", "카카오3"));

        livingOpportunityRepository.save(livingOpportunity);
        System.out.println("complete");
    }
}
