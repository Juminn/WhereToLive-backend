package com.enm.whereToLive.integration.service;

import com.enm.whereToLive.dto.OpportunityRequestDTO;
import com.enm.whereToLive.dto.OpportunityResponseDTO;
import com.enm.whereToLive.exception.ClusterNotFoundException;
import com.enm.whereToLive.service.WhereToLiveService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WhereToLiveServiceIntegrationTest {

    private final WhereToLiveService whereToLiveService;

    @Autowired
    public WhereToLiveServiceIntegrationTest(WhereToLiveService whereToLiveService) {
        this.whereToLiveService = whereToLiveService;
    }

    @Test
    @Transactional
    void myTest() throws ClusterNotFoundException, Exception {

        /*
        given
        */
        OpportunityRequestDTO opportunityRequestDTO = OpportunityRequestDTO.builder()
                .latitude(123)
                .longitude(456)
                .workDays(5)
                .build();

        /*
        when then
        */

        OpportunityResponseDTO opportunityResponseDTO = whereToLiveService.getPlaceOpportunity(opportunityRequestDTO);

        System.out.println(opportunityResponseDTO);

        Assertions.assertEquals(opportunityResponseDTO.getDestination().getName(), "3-3-3-3" );
    }

}
