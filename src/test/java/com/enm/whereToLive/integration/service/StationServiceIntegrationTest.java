package com.enm.whereToLive.integration.service;

import com.enm.whereToLive.dto.OpportunityRequestDTO;
import com.enm.whereToLive.service.StationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StationServiceIntegrationTest {

    private final StationService stationService;

    @Autowired
    public StationServiceIntegrationTest(StationService stationService) {
        this.stationService = stationService;
    }

    @Test
    @Transactional
    void myTest() {

        /*
        given
        */
        //IDT위치
        OpportunityRequestDTO opportunityRequestDTO = OpportunityRequestDTO.builder()
                .latitude(37.545348562499996)
                .longitude(126.81842368750002)
                .workdays(5)
                .build();

        //stationService.getStationById()

        /*
        when then
        */

//        OpportunityResponseDTO opportunityResponseDTO = whereToLiveService.getPlaceOpportunity(opportunityRequestDTO);
//
//        System.out.println(opportunityResponseDTO);
//
//        Assertions.assertEquals(opportunityResponseDTO.getDestination().getName(), "0-2-3" );
    }

}
