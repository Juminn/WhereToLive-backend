package com.enm.whereToLive.service.whenToGo;

import com.enm.whereToLive.data.Destination;
import com.enm.whereToLive.data.GoingWorkDTO;
import com.enm.whereToLive.data.Station;
import com.enm.whereToLive.data.whenToGo.WhenToGoRequestDTO;
import com.enm.whereToLive.data.whenToGo.WhenToGoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WhenToGoService {
    private WhenToGoApiClient whenToGoApiClient;

    @Autowired
    WhenToGoService(WhenToGoApiClient whenToGoApiClient){
        this.whenToGoApiClient = whenToGoApiClient;
    }

    public GoingWorkDTO getGoingWorkOpportunity(Station station, Destination destination){

        double startX = station.getLongitude();
        double startY = station.getLatitude();
        double goalX = destination.getLng();
        double goalY = destination.getLat();
        String startTime = "2024-08-01T08:00";
        String endTime = "2024-08-01T08:00"; //수정필요
        int transferCost = 2000;
        int subwayCost = 5000;
        int busCost = 7000;
        int walkingCost = 10000;
        WhenToGoRequestDTO whenToGoRequestDTO = new WhenToGoRequestDTO(startX, startY, goalX, goalY,startTime, endTime, transferCost, subwayCost, busCost, walkingCost);

        WhenToGoResponseDTO whenToGoResponseDTO = whenToGoApiClient.getGoingOpportunity(whenToGoRequestDTO);
        GoingWorkDTO goingWorkDTO = new GoingWorkDTO();
        goingWorkDTO.setCost(whenToGoResponseDTO.getMinCost());
        goingWorkDTO.setDuration(whenToGoResponseDTO.getMinDuration());

        return goingWorkDTO;
    }

}
