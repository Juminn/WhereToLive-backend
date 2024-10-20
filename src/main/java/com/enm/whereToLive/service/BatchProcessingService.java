package com.enm.whereToLive.service;

import com.enm.whereToLive.data.Destination;
import com.enm.whereToLive.data.GoingWorkDTO;
import com.enm.whereToLive.data.Station;
import com.enm.whereToLive.data.cluster.Cluster;
import com.enm.whereToLive.data.entity.LivingOpportunity;
import com.enm.whereToLive.data.repository.ClusterRepository;
import com.enm.whereToLive.service.whenToGo.WhenToGoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BatchProcessingService {

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private WhenToGoService whenToGoService;

    @Autowired
    private StationService stationService;

    // 매일 자정에 실행
    //@Scheduled(cron = "0 0 0 * * *")
    @PostConstruct
    public void processDailyClusters() throws Exception {
        int dailyLimit = 16;

        // 1. 클러스터 분할 및 생성
        clusterService.generateDailyClusters(dailyLimit);

        // 2. 오늘 생성된 클러스터 가져오기
        List<Cluster> newClusters = clusterRepository.findClustersCreatedToday();

        // 3. 기회 비용 계산 및 저장
        for (Cluster cluster : newClusters) {
            double centerLat = (cluster.getMinLatitude() + cluster.getMaxLatitude()) / 2;
            double centerLon = (cluster.getMinLongitude() + cluster.getMaxLongitude()) / 2;

            Destination destination = new Destination(cluster.getId().toString(), centerLat, centerLon );
            batchMakeOpportunity(destination);

            clusterRepository.save(cluster);
        }
    }

    public ArrayList<Station> batchMakeOpportunity(Destination destination) throws Exception {
        ArrayList<Station> stationList = stationService.getAllStations();

        //String stationsRental = getStationsRental(stationList);
        makeStationsTotalOpportunity(stationList, destination);

        return stationList;

    }

    public void makeStationsTotalOpportunity(ArrayList<Station> stationList, Destination destination){
        ArrayList<LivingOpportunity> livingOpportunities = new ArrayList<>();

        for(Station station:stationList) {

            GoingWorkDTO goingWorkDTO = whenToGoService.getGoingWorkOpportunity(station, destination);
            int monthlyGoingWorkOpportunity = (goingWorkDTO.getCost() * 20 * 2) / 10000; // 월 20회, 왕복 2회, 만원 단위

            if(station.getMontlyRent() != null) {
                int monthlyRent = station.getMontlyRent().intValue();
                int monthlyTotalOpportunity = monthlyGoingWorkOpportunity + monthlyRent;

                System.out.println("stationID: " + station.getId() +
                        "totalOpportunity: " + monthlyTotalOpportunity +
                        " monthlyGoingWorkOpportunity: " + monthlyGoingWorkOpportunity +
                        " monthlyRent: " + monthlyRent +
                        " goingWorkMinute: " + goingWorkDTO.getDuration()
                );

                //test
                LivingOpportunity livingOpportunity = new LivingOpportunity();
                livingOpportunity.setDestination(destination.getName());
                livingOpportunity.setStationID(station.getId());
                livingOpportunity.setTotalOpportunityCost(monthlyTotalOpportunity);
                livingOpportunity.setCommuteCost(monthlyGoingWorkOpportunity);
                livingOpportunity.setCommuteTime(goingWorkDTO.getDuration());
                livingOpportunity.setStationName(station.getName());
                livingOpportunity.setLine(station.getLine());
                livingOpportunity.setLatitude(destination.getLat());
                livingOpportunity.setLongitude(destination.getLng());
                livingOpportunity.setRentCost(monthlyRent);

                //DB저장 코드필요
            }
            else{
                System.out.println(
                        " monthlyGoingWorkOpportunity: " + monthlyGoingWorkOpportunity
                );

            }

        }

    }
}
