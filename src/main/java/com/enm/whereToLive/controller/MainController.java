package com.enm.whereToLive.controller;

import com.enm.whereToLive.dto.OpportunityRequestDTO;
import com.enm.whereToLive.dto.OpportunityRequestDTO2;
import com.enm.whereToLive.exception.ClusterNotFoundException;
import com.enm.whereToLive.dto.OpportunityResponseDTO;
import com.enm.whereToLive.dto.OpportunityResponseDTO2;
import com.enm.whereToLive.exception.NoLivingOpportunitiesException;
import com.enm.whereToLive.service.BatchDabangAndManual;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.TestService;
import com.enm.whereToLive.service.WhereToLiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private BatchDabangAndManual batchService;
    private WhereToLiveService whereToLiveService;
    private TestService testService;
    private StationService stationService;

    @Autowired
    public MainController(BatchDabangAndManual batchService, WhereToLiveService whereToLiveService, TestService testService, StationService stationService){
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
        testService.test();

        return null;
    }

    @GetMapping("/opportunity")
    public OpportunityResponseDTO opportunity(OpportunityRequestDTO opportunityRequestDTO) throws ClusterNotFoundException {
        
        return whereToLiveService.getPlaceOpportunity(opportunityRequestDTO);
    }

    @GetMapping("/opportunity2")
    public OpportunityResponseDTO2 opportunity2(OpportunityRequestDTO2 opportunityRequestDTO2) {

        return whereToLiveService.getPlaceOpportunity2(opportunityRequestDTO2);
    }
}
