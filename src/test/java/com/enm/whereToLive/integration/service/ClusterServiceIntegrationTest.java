package com.enm.whereToLive.integration.service;

import com.enm.whereToLive.dto.OpportunityRequestDTO;
import com.enm.whereToLive.dto.OpportunityResponseDTO;
import com.enm.whereToLive.entity.ClusterEntity;
import com.enm.whereToLive.exception.ClusterNotFoundException;
import com.enm.whereToLive.service.ClusterService;
import com.enm.whereToLive.service.WhereToLiveService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClusterServiceIntegrationTest {

    private final ClusterService clusterService;

    @Autowired
    public ClusterServiceIntegrationTest(ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    @Test
    @Transactional
    void myTest() throws ClusterNotFoundException, Exception {

        /*
        given
        */
        //IDT위치
        Double latitude = 37.545348562499996;
        Double longtitude = 126.81842368750002;

        /*
        when then
        */

        ClusterEntity clusterEntity = clusterService.findClusterByCoordinates(latitude, longtitude);

        System.out.println(clusterEntity);

        Assertions.assertEquals(clusterEntity.getId(), "0-2-3" );
    }

}
