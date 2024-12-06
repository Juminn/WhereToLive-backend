package com.enm.whereToLive.controller;

import com.enm.whereToLive.dto.OpportunityRequestDTO;
import com.enm.whereToLive.exception.ClusterNotFoundException;
import com.enm.whereToLive.model.Destination;
import com.enm.whereToLive.model.Station;
import com.enm.whereToLive.dto.OpportunityResponseDTO;
import com.enm.whereToLive.dto.OpportunityResponseDTO2;
import com.enm.whereToLive.service.BatchServiceOld;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.TestService;
import com.enm.whereToLive.service.WhereToLiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class MainController {

    private BatchServiceOld batchService;
    private WhereToLiveService whereToLiveService;
    private TestService testService;
    private StationService stationService;

    @Autowired
    public MainController(BatchServiceOld batchService, WhereToLiveService whereToLiveService, TestService testService, StationService stationService){
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
    public OpportunityResponseDTO opportunity(OpportunityRequestDTO opportunityRequestDTO) throws Exception, ClusterNotFoundException {
        
        return whereToLiveService.getPlaceOpportunity(opportunityRequestDTO);
    }

    @GetMapping("/opportunity2")
    public OpportunityResponseDTO2 opportunity2(@RequestParam String company, @RequestParam int workdays) throws Exception {

        return whereToLiveService.getPlaceOpportunity2(company, workdays);
    }

    @GetMapping("/batch")
    public ArrayList<Station> batchMakeOpportunity() throws Exception {

        Destination destination = new Destination();
        destination.setLat(37.5113373);
        destination.setLng(127.0665525);

        return batchService.batchMakeOpportunityDemo(destination);

    }


}
