package com.enm.whereToLive.service;

import com.enm.whereToLive.data.Destination;
import com.enm.whereToLive.data.GoingWorkDTO;
import com.enm.whereToLive.data.Station;
import com.enm.whereToLive.data.cluster.Cluster;
import com.enm.whereToLive.data.cluster.ClusterStatus;
import com.enm.whereToLive.data.cluster.LivingOpportunity2;
import com.enm.whereToLive.data.cluster.LivingOpportunityId2;
import com.enm.whereToLive.data.entity.LivingOpportunity;
import com.enm.whereToLive.data.repository.ClusterRepository;
import com.enm.whereToLive.data.repository.LivingOpportunityRepository2;
import com.enm.whereToLive.service.whenToGo.WhenToGoService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BatchProcessingService {

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private LivingOpportunityRepository2 livingOpportunityRepository2;

    @Autowired
    private WhenToGoService whenToGoService;

    @Autowired
    private StationService stationService;

    private static final Logger logger = LoggerFactory.getLogger(ClusterService.class);

    // 매일 자정에 실행
    //@Scheduled(cron = "0 0 0 * * *")
    @PostConstruct
    public void processDailyClusters() throws Exception {

        while (true) {
            // 대기중인 클러스터 가져오기
            Optional<Cluster> optionalNewCluster = clusterRepository.findFirstByStatus(ClusterStatus.PENDING);
            Cluster newCluster;

            if (optionalNewCluster.isEmpty()){
                logger.info("No PENDING Cluster, so Generate Cluster");

                // 클러스터 분할 및 생성
                clusterService.generateDailyClusters();
                continue;
            }
            else {
                newCluster = optionalNewCluster.get();
            }

            // 3. 기회 비용 계산 및 저장
            newCluster.setStatus(ClusterStatus.PROCESSING);
            clusterRepository.save(newCluster);

            double centerLat = (newCluster.getMinLatitude() + newCluster.getMaxLatitude()) / 2;
            double centerLon = (newCluster.getMinLongitude() + newCluster.getMaxLongitude()) / 2;

            Destination destination = new Destination(newCluster.getId().toString(), centerLat, centerLon);
            //batchMakeOpportunity(destination);

            newCluster.setStatus(ClusterStatus.CAL_COMPLETED);
            clusterRepository.save(newCluster);
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
                LivingOpportunity2 livingOpportunity = new LivingOpportunity2();

                LivingOpportunityId2 livingOpportunityId = new LivingOpportunityId2();
                livingOpportunityId.setDestination(destination.getName());
                livingOpportunityId.setStationId(station.getId());

                livingOpportunity.setId(livingOpportunityId);
                livingOpportunity.setTotalOpportunityCost(monthlyTotalOpportunity);
                livingOpportunity.setCommuteCost(monthlyGoingWorkOpportunity);
                livingOpportunity.setCommuteTime(goingWorkDTO.getDuration());
                livingOpportunity.setStationName(station.getName());
                livingOpportunity.setLine(station.getLine());
                livingOpportunity.setLatitude(destination.getLat());
                livingOpportunity.setLongitude(destination.getLng());
                livingOpportunity.setRentCost(monthlyRent);

                //DB저장 코드필요
                livingOpportunityRepository2.save(livingOpportunity);
            }
            else{
                System.out.println(
                        " monthlyGoingWorkOpportunity: " + monthlyGoingWorkOpportunity
                );

            }

        }

    }
}
