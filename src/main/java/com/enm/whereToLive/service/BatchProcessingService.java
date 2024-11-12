package com.enm.whereToLive.service;

import com.enm.whereToLive.model.Destination;
import com.enm.whereToLive.dto.GoingWorkDTO;
import com.enm.whereToLive.model.Station;
import com.enm.whereToLive.entity.ClusterEntity;
import com.enm.whereToLive.entity.LivingOpportunityEntityMySQL;
import com.enm.whereToLive.entity.LivingOpportunityEntityMySQLID;
import com.enm.whereToLive.entity.LivingOpportunityEntityDynamo;
import com.enm.whereToLive.repository.mysql.ClusterRepository;
import com.enm.whereToLive.repository.mysql.LivingOpportunityRepository2;
import com.enm.whereToLive.api.whenToGo.service.WhenToGoService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
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

    // 매일 자정에 실행
    //@Scheduled(cron = "0 0 0 * * *")
    //@PostConstruct
    public void processDailyClusters() throws Exception {

        while (true) {
            // 진행 중 종료된 클러스터 가져오기
            Optional<ClusterEntity> optionalNewCluster = clusterRepository.findFirstByStatus(ClusterEntity.Status.PROCESSING);
            ClusterEntity newClusterEntity;

            // 대기중인 클러스터 가져오기
            if(optionalNewCluster.isEmpty()) {
                optionalNewCluster = clusterRepository.findFirstByStatus(ClusterEntity.Status.PENDING);
            }
            if (optionalNewCluster.isEmpty()){
                log.info("No PENDING Cluster, so Generate Cluster");

                // 클러스터 분할 및 생성
                clusterService.generateDailyClusters();
                continue;
            }
            else {
                newClusterEntity = optionalNewCluster.get();
            }

            // 3. 기회 비용 계산 및 저장
            newClusterEntity.setStatus(ClusterEntity.Status.PROCESSING);
            clusterRepository.save(newClusterEntity);

            double centerLat = (newClusterEntity.getMinLatitude() + newClusterEntity.getMaxLatitude()) / 2;
            double centerLon = (newClusterEntity.getMinLongitude() + newClusterEntity.getMaxLongitude()) / 2;

            Destination destination = new Destination(newClusterEntity.getId(), centerLat, centerLon);
            batchMakeOpportunity(destination);

            newClusterEntity.setStatus(ClusterEntity.Status.CAL_COMPLETED);
            clusterRepository.save(newClusterEntity);
        }
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

                log.info("stationID: " + station.getId() +
                        "totalOpportunity: " + monthlyTotalOpportunity +
                        " monthlyGoingWorkOpportunity: " + monthlyGoingWorkOpportunity +
                        " monthlyRent: " + monthlyRent +
                        " goingWorkMinute: " + goingWorkDTO.getDuration()
                );

                //test
                LivingOpportunityEntityMySQL livingOpportunity = new LivingOpportunityEntityMySQL();

                LivingOpportunityEntityMySQLID livingOpportunityId = new LivingOpportunityEntityMySQLID();
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
