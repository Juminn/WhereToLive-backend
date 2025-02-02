package com.enm.whereToLive.service;

import com.enm.whereToLive.repository.dynamo.LivingOpportunityRepository;
import com.enm.whereToLive.api.dabang.service.DabangService;
import com.enm.whereToLive.api.whenToGo.service.WhenToGoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private final BatchDabangAndManual batchDabangAndManual;
    private StationService stationService;
    private DabangService dabangService;
    private WhenToGoService whenToGoService;
    private LivingOpportunityRepository livingOpportunityRepository;

    @Autowired
    public TestService(StationService stationService, DabangService dabangService, WhenToGoService whenToGoService, LivingOpportunityRepository livingOpportunityRepository, BatchDabangAndManual batchDabangAndManual) {
        this.stationService = stationService;
        this.dabangService = dabangService;
        this.whenToGoService = whenToGoService;
        this.livingOpportunityRepository = livingOpportunityRepository;
        this.batchDabangAndManual = batchDabangAndManual;
    }


    public void test() throws Exception {

        //수동배치 테스트
        //batchDabangAndManual.batchMakeOpportunity(new Destination("현대오토에버", 37.5113373,127.0665525));

        System.out.println("complete");
    }
}
