package com.enm.whereToLive.integration.repository;

import com.enm.whereToLive.dto.OpportunityRequestDTO;
import com.enm.whereToLive.entity.ClusterEntity;
import com.enm.whereToLive.entity.LivingOpportunityEntityMySQL;
import com.enm.whereToLive.exception.ClusterNotFoundException;
import com.enm.whereToLive.repository.mysql.LivingOpportunityRepository2;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@DisplayName("통합테스트::리포지토리::LivingOpportunityRepository2")
public class LivingOpportunityRepository2IntegrationTest {

    private final LivingOpportunityRepository2 livingOpportunityRepository2;

    @Autowired
    public LivingOpportunityRepository2IntegrationTest(LivingOpportunityRepository2 livingOpportunityRepository2) {
        this.livingOpportunityRepository2 = livingOpportunityRepository2;
    }

    @Test
    @Transactional
    @DisplayName("findByIdDestination::정상케이스")
    void myTest() {

        /*
        given
        */
        //IDT위치
        String clusterId = "0-2-3";

        /*
        when then
        */
        List<LivingOpportunityEntityMySQL> livingOpportunityEntityMySQLS = livingOpportunityRepository2.findByIdDestination(clusterId);

        System.out.println(livingOpportunityEntityMySQLS);

        Assertions.assertEquals(livingOpportunityEntityMySQLS.get(0).getId().getDestination(), "0-2-3" );
    }

}
