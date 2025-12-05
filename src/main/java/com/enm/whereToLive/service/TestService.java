package com.enm.whereToLive.service;

import com.enm.whereToLive.repository.dynamo.LivingOpportunityRepository;
import com.enm.whereToLive.external.dabang.service.DabangService;
import com.enm.whereToLive.external.whenToGo.service.WhenToGoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private final BatchDabangAndManual batchDabangAndManual;
    private StationService stationService;
    private DabangService dabangService;
    private WhenToGoService whenToGoService;
    private LivingOpportunityRepository livingOpportunityRepository;

    private BatchClusterAndAuto batchClusterAndAuto;

    @Autowired
    public TestService(StationService stationService, DabangService dabangService, WhenToGoService whenToGoService, LivingOpportunityRepository livingOpportunityRepository, BatchDabangAndManual batchDabangAndManual, BatchClusterAndAuto batchClusterAndAuto) {
        this.stationService = stationService;
        this.dabangService = dabangService;
        this.whenToGoService = whenToGoService;
        this.livingOpportunityRepository = livingOpportunityRepository;
        this.batchDabangAndManual = batchDabangAndManual;
        this.batchClusterAndAuto = batchClusterAndAuto;
    }


    public void test() throws Exception {

        batchClusterAndAuto.processDailyClusters();

        System.out.println("complete");
    }
}
