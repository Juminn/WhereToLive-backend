package com.enm.whereToLive.integration.service;

import com.enm.whereToLive.dto.OpportunityRequestDTO;
import com.enm.whereToLive.service.StationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("통합테스트::서비스::StationService")
public class StationServiceIntegrationTest {

    private final StationService stationService;

    @Autowired
    public StationServiceIntegrationTest(StationService stationService) {
        this.stationService = stationService;
    }

}
