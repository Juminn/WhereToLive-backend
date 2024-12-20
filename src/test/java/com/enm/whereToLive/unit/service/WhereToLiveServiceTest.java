package com.enm.whereToLive.unit.service;

import ch.qos.logback.core.testUtil.MockInitialContext;
import com.enm.whereToLive.api.dabang.service.DabangService;
import com.enm.whereToLive.api.whenToGo.service.WhenToGoService;
import com.enm.whereToLive.dto.OpportunityRequestDTO;
import com.enm.whereToLive.dto.OpportunityResponseDTO;
import com.enm.whereToLive.entity.ClusterEntity;
import com.enm.whereToLive.entity.LivingOpportunityEntityMySQL;
import com.enm.whereToLive.entity.LivingOpportunityEntityMySQLID;
import com.enm.whereToLive.exception.ClusterNotFoundException;
import com.enm.whereToLive.model.Destination;
import com.enm.whereToLive.model.Station;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    List<Station> stations;

    // Test를 실행하기 전마다 MemberService에 가짜 객체를 주입시켜준다.
    @BeforeEach
    void setUp(){
        whereToLiveService = new WhereToLiveServiceImpl(stationService, dabangService, whenToGoService, livingOpportunityRepository, livingOpportunityRepository2, clusterService);
        stations = generateTestStations(700); // 700개 Station 객체 생성
    }

    private List<Station> generateTestStations(int count) {
        List<Station> stationList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Station station = new Station();
            station.setId(i); // 고유 ID 설정
            station.setLatitude(37.545348562499996); // 예시 위도
            station.setLongitude(126.81842368750002); // 예시 경도
            station.setPros("Pros for station " + i);
            station.setCons("Cons for station " + i);
            stationList.add(station);
        }
        return stationList;
    }

    @Test
    @DisplayName("멤버 생성 성공")
    void createMemberSuccess() throws Exception, ClusterNotFoundException {

        /*
        given
         */
        double latitude = 37.545348562499996;
        double longtitude = 126.81842368750002;
        int workdays = 5;

        //clusterService세팅, IDT위치
        OpportunityRequestDTO opportunityRequestDTO = OpportunityRequestDTO.builder()
                .latitude(latitude)
                .longitude(longtitude)
                .workDays(workdays)
                .build();

        ClusterEntity clusterEntity = ClusterEntity.builder()
                .id("0-2-3")
                .level(2)
                .minLatitude(37.526483625)
                .maxLatitude(37.5642135)
                .minLongitude(126.79031112500002)
                .maxLongitude(126.84653625000001)
                .parentId("0-2")
                .createdAt(LocalDateTime.parse(("2024-10-27T08:45:39.687480")))
                .build();

        Mockito.when(clusterService.findClusterByCoordinates(latitude, longtitude)).thenReturn(clusterEntity);

        //repository세팅
        String clusterId = "0-2-3";

        LivingOpportunityEntityMySQL livingOpportunityEntityMySQL = LivingOpportunityEntityMySQL.builder()
                .id(new LivingOpportunityEntityMySQLID("0-2-3", 1))
                .stationName("녹양역")
                .line("1호선")
                .latitude(37.545348562499996)
                .longitude(126.81842368750002)
                .rentCost(36)
                .commuteCost(56)
                .totalOpportunityCost(92)
                .commuteTime(121)
                .pros(null)
                .cons(null)
                .build();

        List<LivingOpportunityEntityMySQL> livingOpportunityEntityMySQLS = Collections.singletonList(livingOpportunityEntityMySQL);

        Mockito.when(livingOpportunityRepository2.findByIdDestination(clusterId)).thenReturn(livingOpportunityEntityMySQLS);

        //StationService세팅
        // Station 응답 설정
        for (LivingOpportunityEntityMySQL opportunity : livingOpportunityEntityMySQLS) {
            Station station = stations.get(opportunity.getId().getStationId() % 700); // 700개 중 하나 선택
            Mockito.when(stationService.getStationById(opportunity.getId().getStationId())).thenReturn(station);
        }


        /*
        when
         */
        OpportunityResponseDTO responseDTO = whereToLiveService.getPlaceOpportunity(opportunityRequestDTO);

        /*
        then
         */

        // Destination 검증
        Destination destination = responseDTO.getDestination();
        assertNotNull(destination, "Destination should not be null");
        assertEquals(clusterEntity.getId(), destination.getName(), "Destination ID should match cluster ID");
        assertEquals(livingOpportunityEntityMySQLS.get(0).getLatitude(), destination.getLat(), "Destination latitude should match");
        assertEquals(livingOpportunityEntityMySQLS.get(0).getLongitude(), destination.getLng(), "Destination longitude should match");
    }

}