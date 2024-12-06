package com.enm.whereToLive.unit.service;

import ch.qos.logback.core.testUtil.MockInitialContext;
import com.enm.whereToLive.api.dabang.service.DabangService;
import com.enm.whereToLive.api.whenToGo.service.WhenToGoService;
import com.enm.whereToLive.dto.OpportunityRequestDTO;
import com.enm.whereToLive.entity.ClusterEntity;
import com.enm.whereToLive.exception.ClusterNotFoundException;
import com.enm.whereToLive.repository.dynamo.LivingOpportunityRepository;
import com.enm.whereToLive.repository.mysql.LivingOpportunityRepository2;
import com.enm.whereToLive.service.ClusterService;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.WhereToLiveService;
import com.enm.whereToLive.service.impl.WhereToLiveServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class WhereToLiveServiceTest {

    // Test 주체
    WhereToLiveService whereToLiveService;

    // Test 협력자
    @MockBean
    StationService stationService;

    @MockBean
    private DabangService dabangService;

    @MockBean
    private WhenToGoService whenToGoService;

    @MockBean
    LivingOpportunityRepository livingOpportunityRepository;

    @MockBean
    LivingOpportunityRepository2 livingOpportunityRepository2;

    @MockBean
    ClusterService clusterService;

    // Test를 실행하기 전마다 MemberService에 가짜 객체를 주입시켜준다.
    @BeforeEach
    void setUp(){
        whereToLiveService = new WhereToLiveServiceImpl(stationService, dabangService, whenToGoService, livingOpportunityRepository, livingOpportunityRepository2, clusterService);
    }

    @Test
    @DisplayName("멤버 생성 성공")
    void createMemberSuccess() throws Exception, ClusterNotFoundException {
    /*
    given
     */
        double latitude = 37.5416401;
        double longtitude = 126.9653684;
        int workdays = 5;

        OpportunityRequestDTO opportunityRequestDTO = OpportunityRequestDTO.builder()
                .latitude(latitude)
                .longitude(longtitude)
                .workDays(workdays)
                .build();

//        ClusterEntity clusterEntity = ClusterEntity.builder()
//                .createdAt()
//                .build();


        Mockito.when(clusterService.findClusterByCoordinates(latitude, longtitude)).thenReturn(new ClusterEntity());

    /*
    when
     */
        //whereToLiveService.getPlaceOpportunity(opportunityRequestDTO);

    /*
    then
     */
        //assertThat(hi3).isEqualTo(3L);
    }

}