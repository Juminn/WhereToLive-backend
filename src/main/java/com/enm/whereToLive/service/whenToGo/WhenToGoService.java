package com.enm.whereToLive.service.whenToGo;

import com.enm.whereToLive.data.Destination;
import com.enm.whereToLive.data.GoingWorkDTO;
import com.enm.whereToLive.data.Station;
import com.enm.whereToLive.data.whenToGo.WhenToGoRequestDTO;
import com.enm.whereToLive.data.whenToGo.WhenToGoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        GoingWorkDTO goingWorkDTO = new GoingWorkDTO();

        try {
            WhenToGoResponseDTO whenToGoResponseDTO = whenToGoApiClient.getGoingOpportunity(whenToGoRequestDTO);
            goingWorkDTO.setCost(whenToGoResponseDTO.getMinCost());
            goingWorkDTO.setDuration(whenToGoResponseDTO.getMinDuration());
        } catch (ResponseStatusException e) {
            System.out.println(e);
            //500미터 보다 범위가 작은 경우 리턴을 못받기때문에 예외처리
            double distance = calculateDistance(startY, startX, goalY, goalX);
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST && distance < 500) {
                System.out.println("Exception처리 거리가 짧아서 기본 처리 거리 ~500m: " + distance);
                goingWorkDTO.setCost(1000);
                goingWorkDTO.setDuration(5);
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST && distance < 1000)  {
                System.out.println("Exception처리 거리가 짧아서 기본 처리 거리 500m~1km: " + distance);
                goingWorkDTO.setCost(2000);
                goingWorkDTO.setDuration(10);
            }
            else {
                System.out.println(distance);
                throw e; // Rethrow other exceptions
            }
        }

        return goingWorkDTO;
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371 * 1000; // Radius of the earth in meters
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in meters
        return distance;
    }

}
