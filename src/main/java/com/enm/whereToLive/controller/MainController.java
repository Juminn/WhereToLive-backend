package com.enm.whereToLive.controller;

import com.enm.whereToLive.data.Destination;
import com.enm.whereToLive.data.Station;
import com.enm.whereToLive.service.BatchService;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.TestService;
import com.enm.whereToLive.service.WhereToLiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class MainController {

    private BatchService batchService;
    private WhereToLiveService whereToLiveService;
    private TestService testService;
    private StationService stationService;

    @Autowired
    public MainController(BatchService batchService, WhereToLiveService whereToLiveService, TestService testService, StationService stationService){
        this.batchService = batchService;
        this.whereToLiveService = whereToLiveService;
        this.testService = testService;
        this.stationService = stationService;
    }

    @GetMapping("/")
    public String index() throws Exception {
        //batchService.getStationsRental(stationService.getAllStations())
        testService.test();

        return null;
    }

    @GetMapping("/test")
    public ArrayList<Station> test() throws Exception {

        String companyName = "현대오토에버";

        return whereToLiveService.getStationsOpportunity(companyName);

    }

    @GetMapping("/batch")
    public ArrayList<Station> batchMakeOpportunity() throws Exception {

        Destination destination = new Destination();
        destination.setLat(37.5113373);
        destination.setLng(127.0665525);

        return batchService.batchMakeOpportunityDemo(destination);

    }


}
