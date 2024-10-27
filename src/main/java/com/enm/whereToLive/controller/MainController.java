package com.enm.whereToLive.controller;

import com.enm.whereToLive.data.Destination;
import com.enm.whereToLive.data.Station;
import com.enm.whereToLive.data.entity.LivingOpportunity;
import com.enm.whereToLive.data.opportunityResponseDTO;
import com.enm.whereToLive.service.BatchService;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.TestService;
import com.enm.whereToLive.service.WhereToLiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    public String index(){

        return "healthCheck";
    }

    @GetMapping("/test")
    public String test() throws Exception {
        //batchService.getStationsRental(stationService.getAllStations())
        testService.test();

        return null;
    }

    @GetMapping("/opportunity")
    public opportunityResponseDTO opportunity(@RequestParam String company, @RequestParam int workdays) throws Exception {
        
        return whereToLiveService.getPlaceOpportunity(company, workdays);
    }

    @GetMapping("/batch")
    public ArrayList<Station> batchMakeOpportunity() throws Exception {

        Destination destination = new Destination();
        destination.setLat(37.5113373);
        destination.setLng(127.0665525);

        return batchService.batchMakeOpportunityDemo(destination);

    }


}
