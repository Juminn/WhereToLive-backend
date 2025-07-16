package com.enm.whereToLive.slice.web;

import com.enm.whereToLive.controller.MainController;
import com.enm.whereToLive.dto.response.OpportunityResponseDTO;
import com.enm.whereToLive.domain.entity.LivingOpportunityEntityMySQL;
import com.enm.whereToLive.exception.ClusterNotFoundException;
import com.enm.whereToLive.dto.Destination;
import com.enm.whereToLive.service.BatchDabangAndManual;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.TestService;
import com.enm.whereToLive.service.WhereToLiveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
@DisplayName("WebSlice::MainController")
class MainControllerWebTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;

    @MockBean
    WhereToLiveService whereToLiveService;
    @MockBean
    BatchDabangAndManual batchService;
    @MockBean
    TestService testService;
    @MockBean
    StationService stationService;

    @Test
    @DisplayName("GET /opportunity – 정상 200")
    void opportunity_validParams_returns200() throws Exception {
        // given
        Destination dest = Destination.builder()
                .name("0-2-3").lat(37.5).lng(126.8).build();

        LivingOpportunityEntityMySQL lo = LivingOpportunityEntityMySQL.builder()
                .stationName("녹양역").totalOpportunityCost(90).build();

        OpportunityResponseDTO dummy = OpportunityResponseDTO.builder()
                .destination(dest)
                .livingOpportunities(List.of(lo))   // ← 최소 1건
                .build();

        Mockito.when(whereToLiveService.getPlaceOpportunity(Mockito.any()))
                .thenReturn(dummy);          // ★ any() 로 매칭

        // when - then
        mvc.perform(get("/opportunity")
                        .param("latitude", "37.5")
                        .param("longitude", "126.8")
                        .param("workdays", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destination").exists());
    }

    @Test
    @DisplayName("GET /opportunity – 클러스터 없음 → 404")
    void opportunity_clusterNotFound_returns404() throws Exception {
        Mockito.when(whereToLiveService.getPlaceOpportunity(Mockito.any()))
                .thenThrow(new ClusterNotFoundException("Cluster not found"));

        mvc.perform(get("/opportunity")
                        .param("latitude", "37.5")
                        .param("longitude", "126.8")
                        .param("workdays", "5"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /opportunity – workdays=8 → 400 Validation")
    void opportunity_invalidWorkdays_returns400() throws Exception {
        mvc.perform(get("/opportunity")
                        .param("latitude", "37.5")
                        .param("longitude", "126.8")
                        .param("workdays", "8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
