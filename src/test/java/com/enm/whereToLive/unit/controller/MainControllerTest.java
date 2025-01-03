package com.enm.whereToLive.unit.controller;

import com.enm.whereToLive.controller.MainController;
import com.enm.whereToLive.dto.OpportunityRequestDTO;
import com.enm.whereToLive.dto.OpportunityResponseDTO;
import com.enm.whereToLive.exception.ClusterNotFoundException;
import com.enm.whereToLive.service.BatchServiceOld;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.TestService;
import com.enm.whereToLive.service.WhereToLiveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
class MainControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private BatchServiceOld batchService;

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
    void opportunity() throws ClusterNotFoundException, Exception {
        /*
        given
         */
        OpportunityRequestDTO opportunityRequestDTO = OpportunityRequestDTO.builder()
                .latitude(123)
                .longitude(456)
                .workdays(5)
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(opportunityRequestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.livingOpportunities").isEmpty()) // livingOpportunities가 null이므로 비어있어야 함
//                .andExpect(MockMvcResultMatchers.jsonPath("$.destination").isEmpty()); // destination이 null이므로 비어있어야 함

    }

    @Test
    void opportunity2() {
    }

    @Test
    void batchMakeOpportunity() {
    }
}