package com.enm.whereToLive.integration.controller;

import com.enm.whereToLive.dto.OpportunityRequestDTO;
import com.enm.whereToLive.dto.OpportunityRequestDTO2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("통합테스트::컨트롤러::MainController")
public class MainControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Get::/opportunity::정상케이스")
    public void testOpportunity() throws Exception {
        //IDT위치
        OpportunityRequestDTO opportunityRequestDTO = OpportunityRequestDTO.builder()
                .latitude(37.545348562499996)
                .longitude(126.81842368750002)
                .workdays(5)
                .build();

        mockMvc.perform(get("/opportunity")
                        .param("latitude", String.valueOf(opportunityRequestDTO.getLatitude()))
                        .param("longitude", String.valueOf(opportunityRequestDTO.getLongitude()))
                        .param("workdays", String.valueOf(opportunityRequestDTO.getWorkdays()))
                        .contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destination").exists())
                .andExpect(jsonPath("$.destination.name").isString())
                .andExpect(jsonPath("$.destination.lat").isNumber())
                .andExpect(jsonPath("$.destination.lng").isNumber())
                .andExpect(jsonPath("$.livingOpportunities").isArray())
                .andExpect(jsonPath("$.livingOpportunities", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.livingOpportunities[0].stationName").isString())
                .andExpect(jsonPath("$.livingOpportunities[0].line").isString())
                .andExpect(jsonPath("$.livingOpportunities[0].rentCost").isNumber())
                .andExpect(jsonPath("$.livingOpportunities[0].commuteTime").isNumber())
                .andExpect(jsonPath("$.livingOpportunities[0].totalOpportunityCost").isNumber())
                .andExpect(jsonPath("$.livingOpportunities[0].pros").isString())
                .andExpect(jsonPath("$.livingOpportunities[0].cons").isString());
    }

    @Test
    @DisplayName("Get::/opportunity::파라미터 비정상 케이스::workdays=8")
    public void opportunityWithInvalidWorkdays() throws Exception {
        //IDT위치
        OpportunityRequestDTO opportunityRequestDTO = OpportunityRequestDTO.builder()
                .latitude(37.545348562499996)
                .longitude(126.81842368750002)
                .workdays(8)
                .build();

        mockMvc.perform(get("/opportunity")
                        .param("latitude", String.valueOf(opportunityRequestDTO.getLatitude()))
                        .param("longitude", String.valueOf(opportunityRequestDTO.getLongitude()))
                        .param("workdays", String.valueOf(opportunityRequestDTO.getWorkdays()))
                        .contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get::/opportunity2::정상케이스")
    public void testOpportunity2() throws Exception {

        //IDT위치
        OpportunityRequestDTO2 opportunityRequestDTO2 = OpportunityRequestDTO2.builder()
                .company("숭실대학교")
                .workdays(5)
                .build();

        mockMvc.perform(get("/opportunity2")
                        .param("company", opportunityRequestDTO2.getCompany())
                        .param("workdays", String.valueOf(opportunityRequestDTO2.getWorkdays()))
                        .contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destination").exists())
                .andExpect(jsonPath("$.destination.name").isString())
                .andExpect(jsonPath("$.destination.lat").isNumber())
                .andExpect(jsonPath("$.destination.lng").isNumber())
                .andExpect(jsonPath("$.livingOpportunities").isArray())
                .andExpect(jsonPath("$.livingOpportunities", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.livingOpportunities[0].stationName").isString())
                .andExpect(jsonPath("$.livingOpportunities[0].line").isString())
                .andExpect(jsonPath("$.livingOpportunities[0].rentCost").isNumber())
                .andExpect(jsonPath("$.livingOpportunities[0].commuteTime").isNumber())
                .andExpect(jsonPath("$.livingOpportunities[0].totalOpportunityCost").isNumber())
                .andExpect(jsonPath("$.livingOpportunities[0].pros").isString())
                .andExpect(jsonPath("$.livingOpportunities[0].cons").isString());
    }
}
