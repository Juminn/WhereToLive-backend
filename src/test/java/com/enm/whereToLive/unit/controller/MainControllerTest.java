package com.enm.whereToLive.unit.controller;

import com.enm.whereToLive.controller.MainController;
import com.enm.whereToLive.dto.OpportunityRequestDTO;
import com.enm.whereToLive.dto.OpportunityResponseDTO;
import com.enm.whereToLive.exception.ClusterNotFoundException;
import com.enm.whereToLive.service.BatchDabangAndManual;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.TestService;
import com.enm.whereToLive.service.WhereToLiveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(MainController.class)
@DisplayName("단위테스트::컨트롤러::MainController")
class MainControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private BatchDabangAndManual batchService;

    @MockBean
    private WhereToLiveService whereToLiveService;

    @MockBean
    private TestService testService;

    @MockBean
    private StationService stationService;

    private ObjectMapper objectMapper;


    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }
    
    @Test
    @DisplayName("Get::/opportunity::정상 케이스")
    void opportunity() throws Exception {
        /*
        given
         */
        int workdays = 5;

        OpportunityRequestDTO opportunityRequestDTO = OpportunityRequestDTO.builder()
                .latitude(123.0)
                .longitude(456.0)
                .workdays(workdays)
                .build();

        OpportunityResponseDTO opportunityResponseDTO = OpportunityResponseDTO.builder()
                .livingOpportunities(null)
                .destination(null)
                .build();

        Mockito.when(whereToLiveService.getPlaceOpportunity(opportunityRequestDTO)).thenReturn(opportunityResponseDTO);

        /*
        when then
         */
        mvc.perform(MockMvcRequestBuilders.get("/opportunity")
                        .param("latitude", String.valueOf(opportunityRequestDTO.getLatitude()))
                        .param("longitude", String.valueOf(opportunityRequestDTO.getLongitude()))
                        .param("workdays", String.valueOf(opportunityRequestDTO.getWorkdays()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(opportunityRequestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DisplayName("Get::/opportunity::파라미터 workdays 비정상 케이스::8")
    void opportunityWithInvalidWorkdays() throws Exception {
        /*
        given
         */
        OpportunityRequestDTO opportunityRequestDTO = OpportunityRequestDTO.builder()
                .latitude(123.0)
                .longitude(456.0)
                .workdays(8)
                .build();

        OpportunityResponseDTO opportunityResponseDTO = OpportunityResponseDTO.builder()
                .livingOpportunities(null)
                .destination(null)
                .build();

        Mockito.when(whereToLiveService.getPlaceOpportunity(opportunityRequestDTO)).thenReturn(opportunityResponseDTO);

        /*
        when then
         */
        mvc.perform(MockMvcRequestBuilders.get("/opportunity")
                        .param("latitude", String.valueOf(opportunityRequestDTO.getLatitude()))
                        .param("longitude", String.valueOf(opportunityRequestDTO.getLongitude()))
                        .param("workdays", String.valueOf(opportunityRequestDTO.getWorkdays()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(opportunityRequestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void batchMakeOpportunity() {
    }
}