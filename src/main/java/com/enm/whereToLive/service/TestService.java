package com.enm.whereToLive.service;

import com.enm.whereToLive.model.Destination;
import com.enm.whereToLive.repository.dynamo.LivingOpportunityRepository;
import com.enm.whereToLive.api.dabang.service.DabangService;
import com.enm.whereToLive.api.whenToGo.service.WhenToGoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private final BatchServiceOld batchService;
    private StationService stationService;
    private DabangService dabangService;
    private WhenToGoService whenToGoService;
    private LivingOpportunityRepository livingOpportunityRepository;

    @Autowired
    public TestService(StationService stationService, DabangService dabangService, WhenToGoService whenToGoService, LivingOpportunityRepository livingOpportunityRepository, BatchServiceOld batchService) {
        this.stationService = stationService;
        this.dabangService = dabangService;
        this.whenToGoService = whenToGoService;
        this.livingOpportunityRepository = livingOpportunityRepository;
        this.batchService = batchService;
    }


    public void test() throws Exception {

        //오토에버테스트
        batchService.batchMakeOpportunity(new Destination("현대오토에버", 37.5113373,127.0665525));

        //읽기쓰기테스트
//        LivingOpportunity livingOpportunity = new LivingOpportunity(new LivingOpportunityId("카카오", 1), "서울역", "서울1호선", 33.2 , 127.3, 80, 30, 50, 20);
//        livingOpportunityRepository.save(livingOpportunity);
//        LivingOpportunity livingOpportunity1 = livingOpportunityRepository.findById(new LivingOpportunityId("카카오", 1)).get();
//        System.out.println(livingOpportunity1);


        System.out.println("complete");
    }
}
