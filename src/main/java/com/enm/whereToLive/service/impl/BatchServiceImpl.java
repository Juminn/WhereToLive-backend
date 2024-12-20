package com.enm.whereToLive.service.impl;

import com.enm.whereToLive.model.Destination;
import com.enm.whereToLive.dto.GoingWorkDTO;
import com.enm.whereToLive.model.Station;
import com.enm.whereToLive.entity.LivingOpportunityEntityDynamo;
import com.enm.whereToLive.repository.dynamo.LivingOpportunityRepository;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.BatchServiceOld;
import com.enm.whereToLive.api.dabang.service.DabangService;
import com.enm.whereToLive.api.whenToGo.service.WhenToGoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BatchServiceImpl implements BatchServiceOld {

    private StationService stationService;
    private DabangService dabangService;
    private WhenToGoService whenToGoService;
    private LivingOpportunityRepository livingOpportunityRepository;

    @Autowired
    public BatchServiceImpl(StationService stationService, DabangService dabangService, WhenToGoService whenToGoService, LivingOpportunityRepository livingOpportunityRepository) {
        this.stationService = stationService;
        this.dabangService = dabangService;
        this.whenToGoService = whenToGoService;
        this.livingOpportunityRepository = livingOpportunityRepository;
    }

    public ArrayList<Station> batchMakeOpportunity(Destination destination) throws Exception {
        ArrayList<Station> stationList = stationService.getAllStations();

        //String stationsRental = getStationsRental(stationList);
        makeStationsTotalOpportunity(stationList, destination);

        return stationList;

    }

    public void makeStationsTotalOpportunity(ArrayList<Station> stationList, Destination destination){
        ArrayList<LivingOpportunityEntityDynamo> livingOpportunities = new ArrayList<>();

        for(Station station:stationList) {

            GoingWorkDTO goingWorkDTO = whenToGoService.getGoingWorkOpportunity(station, destination);
            int monthlyGoingWorkOpportunity = (goingWorkDTO.getCost() * 20 * 2) / 10000; // 월 20회, 왕복 2회, 만원 단위

            if(station.getMontlyRent() != null) {
                int monthlyRent = station.getMontlyRent().intValue();
                int monthlyTotalOpportunity = monthlyGoingWorkOpportunity + monthlyRent;

                System.out.println(
                        "stationID: " + station.getId() +
                        " Destination: " + destination.getName() +
                        " totalOpportunity: " + monthlyTotalOpportunity +
                        " monthlyGoingWorkOpportunity: " + monthlyGoingWorkOpportunity +
                        " monthlyRent: " + monthlyRent +
                        " goingWorkMinute: " + goingWorkDTO.getDuration()
                );

                //test
                LivingOpportunityEntityDynamo livingOpportunityEntityDynamo = new LivingOpportunityEntityDynamo();
                livingOpportunityEntityDynamo.setDestination(destination.getName());
                livingOpportunityEntityDynamo.setStationID(station.getId());
                livingOpportunityEntityDynamo.setTotalOpportunityCost(monthlyTotalOpportunity);
                livingOpportunityEntityDynamo.setCommuteCost(monthlyGoingWorkOpportunity);
                livingOpportunityEntityDynamo.setCommuteTime(goingWorkDTO.getDuration());
                livingOpportunityEntityDynamo.setStationName(station.getName());
                livingOpportunityEntityDynamo.setLine(station.getLine());
                livingOpportunityEntityDynamo.setLatitude(destination.getLat());
                livingOpportunityEntityDynamo.setLongitude(destination.getLng());
                livingOpportunityEntityDynamo.setRentCost(monthlyRent);

                //임시
                livingOpportunityRepository.save(livingOpportunityEntityDynamo);

                livingOpportunities.add(livingOpportunityEntityDynamo);
            }
            else{
                System.out.println(
                        " monthlyGoingWorkOpportunity: " + monthlyGoingWorkOpportunity
                );

            }

        }
        //livingOpportunityRepository.saveAll(livingOpportunities);
    }

    public ArrayList<Station> batchMakeOpportunityDemo(Destination destination) throws Exception {
        ArrayList<Station> stationList = stationService.getAllStations();

        //String stationsRental = getStationsRental(stationList);
        makeStationsTotalOpportunityDemo(stationList, destination);

        return stationList;

    }

    public void makeStationsTotalOpportunityDemo(ArrayList<Station> stationList, Destination destination){
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
}
