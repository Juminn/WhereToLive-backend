package com.enm.whereToLive.service.impl;

import com.enm.whereToLive.data.Destination;
import com.enm.whereToLive.data.GoingWorkDTO;
import com.enm.whereToLive.data.Station;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.BatchService;
import com.enm.whereToLive.service.dabang.DabangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BatchServiceImpl implements BatchService {

    private StationService stationService;
    private DabangService dabangService;
    private com.enm.whereToLive.service.whenToGo.WhenToGoService whenToGoService;

    @Autowired
    public BatchServiceImpl(StationService stationService, DabangService dabangService, com.enm.whereToLive.service.whenToGo.WhenToGoService whenToGoService) {
        this.stationService = stationService;
        this.dabangService = dabangService;
        this.whenToGoService = whenToGoService;
    }

    public ArrayList<Station> batchMakeOpportunity(Destination destination) throws Exception {
        ArrayList<Station> stationList = stationService.getAllStations();

        //String stationsRental = getStationsRental(stationList);
        makeStationsTotalOpportunity(stationList, destination);

        return stationList;

    }

    //매일 월세 역별 월세 평균치를 가져오는 배치 기능
    public String getStationsRental(ArrayList<Station> stationList) throws Exception {

        String result = "";

        for(Station station: stationList){
            Double stationRental = dabangService.getSubwayStationRental(station, "HOUSE_VILLA", "ONE_ROOM");
            if(stationRental != null) {
                station.setMontlyRent(stationRental);
                result += stationRental + "\n";
            }
            else{
                result += "null\n";
            }
        }

        System.out.println(result);

        return result;

    }

    public void makeStationsTotalOpportunity(ArrayList<Station> stationList, Destination destination){
        for(Station station:stationList) {

            GoingWorkDTO goingWorkDTO = whenToGoService.getGoingWorkOpportunity(station, destination);
            Double monthlyGoingWorkOpportunity = (double) (goingWorkDTO.getCost() * 20 * 2) / 10000; // 월 20회, 왕복 2회, 만원 단위

            if(station.getMontlyRent() != null) {
                Double monthlyRent = station.getMontlyRent();
                Double monthlyTotalOpportunity = monthlyGoingWorkOpportunity + monthlyRent;

                System.out.println("totalOpportunity: " + monthlyTotalOpportunity +
                        " monthlyGoingWorkOpportunity: " + monthlyGoingWorkOpportunity +
                        " monthlyRent: " + monthlyRent +
                        " goingWorkMinute: " + goingWorkDTO.getDuration()
                );

                station.setGoingWorkMinute(goingWorkDTO.getDuration());
                station.setMontlyGoingOpportunity(monthlyGoingWorkOpportunity);
                station.setMontlyTotalOpportunity(monthlyTotalOpportunity);

            }
            else{
                System.out.println(
                        " monthlyGoingWorkOpportunity: " + monthlyGoingWorkOpportunity
                );

                station.setMontlyGoingOpportunity(monthlyGoingWorkOpportunity);
            }
        }
    }
}
