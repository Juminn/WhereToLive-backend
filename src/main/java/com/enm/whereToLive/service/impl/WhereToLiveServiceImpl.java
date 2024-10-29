package com.enm.whereToLive.service.impl;

import com.enm.whereToLive.data.Destination;
import com.enm.whereToLive.data.Station;
import com.enm.whereToLive.data.cluster.Cluster;
import com.enm.whereToLive.data.cluster.LivingOpportunityMySQL;
import com.enm.whereToLive.data.entity.LivingOpportunityDynamo;
import com.enm.whereToLive.data.dynamoDBRepository.LivingOpportunityRepository;
import com.enm.whereToLive.data.opportunityResponseDTO;
import com.enm.whereToLive.data.opportunityResponseDTO2;
import com.enm.whereToLive.data.repository.LivingOpportunityRepository2;
import com.enm.whereToLive.service.ClusterService;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.WhereToLiveService;
import com.enm.whereToLive.service.dabang.DabangService;
import com.enm.whereToLive.service.whenToGo.WhenToGoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class WhereToLiveServiceImpl implements WhereToLiveService {

    private StationService stationService;
    private DabangService dabangService;
    private WhenToGoService whenToGoService;
    private LivingOpportunityRepository livingOpportunityRepository;

    private LivingOpportunityRepository2 livingOpportunityRepository2;
    private ClusterService clusterService;

    private static final Logger logger = LoggerFactory.getLogger(ClusterService.class);

    @Autowired
    public WhereToLiveServiceImpl(StationService stationService, DabangService dabangService, WhenToGoService whenToGoService, LivingOpportunityRepository livingOpportunityRepository, LivingOpportunityRepository2 livingOpportunityRepository2, ClusterService clusterService) {
        this.stationService = stationService;
        this.dabangService = dabangService;
        this.whenToGoService = whenToGoService;
        this.livingOpportunityRepository = livingOpportunityRepository;
        this.livingOpportunityRepository2 = livingOpportunityRepository2;
        this.clusterService = clusterService;
    }

    @Override
    public opportunityResponseDTO getPlaceOpportunity(double latitude, Double longitude, int workDays) {
        opportunityResponseDTO opportunityResponseDTO = new opportunityResponseDTO();

        Cluster cluster = clusterService.findClusterByCoordinates(latitude, longitude);

        logger.info(cluster.toString());

        List<LivingOpportunityMySQL> livingOpportunities = livingOpportunityRepository2.findByIdDestination(cluster.getId());



        Destination destination = new Destination(cluster.getId(), livingOpportunities.get(0).getLatitude(), livingOpportunities.get(0).getLongitude());

        for(LivingOpportunityMySQL livingOpportunity : livingOpportunities) {
            Station station = stationService.getStationById(livingOpportunity.getId().getStationId());

            livingOpportunity.setLatitude(station.getLatitude());
            livingOpportunity.setLongitude(station.getLongitude());
            livingOpportunity.setPros(station.getPros());
            livingOpportunity.setCons(station.getCons());
        }

        livingOpportunities = calPlaceOpportunity(livingOpportunities, workDays);

        opportunityResponseDTO.setLivingOpportunities(livingOpportunities);
        opportunityResponseDTO.setDestination(destination);

        return opportunityResponseDTO;
    }

    @Override
    public opportunityResponseDTO2 getPlaceOpportunity2(String name, int workDays) {
        opportunityResponseDTO2 opportunityResponseDTO = new opportunityResponseDTO2();

        List<LivingOpportunityDynamo> livingOpportunities = livingOpportunityRepository.findByDestination(name);
        Destination destination = new Destination(name, livingOpportunities.get(0).getLatitude(), livingOpportunities.get(0).getLongitude());

        for(LivingOpportunityDynamo livingOpportunityDynamo : livingOpportunities) {
            Station station = stationService.getStationById(livingOpportunityDynamo.getStationID());

            livingOpportunityDynamo.setLatitude(station.getLatitude());
            livingOpportunityDynamo.setLongitude(station.getLongitude());
            livingOpportunityDynamo.setPros(station.getPros());
            livingOpportunityDynamo.setCons(station.getCons());
        }

        livingOpportunities = calPlaceOpportunity2(livingOpportunities, workDays);

        opportunityResponseDTO.setLivingOpportunities(livingOpportunities);
        opportunityResponseDTO.setDestination(destination);

        return opportunityResponseDTO;
    }

    private List<LivingOpportunityMySQL> calPlaceOpportunity(List<LivingOpportunityMySQL> livingOpportunities, int workDays) {
        // PaginatedList를 ArrayList로 복사
        List<LivingOpportunityMySQL> opportunityList = new ArrayList<>(livingOpportunities);

        for (LivingOpportunityMySQL opportunity : opportunityList) {
            // commuteCost 계산 및 할당
            int commuteCost = (int) (opportunity.getCommuteCost() * ((double) workDays / 5));
            opportunity.setCommuteCost(commuteCost);

            // totalOpportunityCost 계산 및 할당
            int totalOpportunityCost = commuteCost + opportunity.getRentCost();
            opportunity.setTotalOpportunityCost(totalOpportunityCost);
        }

        // totalOpportunityCost를 기준으로 정렬
        opportunityList.sort(Comparator.comparingInt(LivingOpportunityMySQL::getTotalOpportunityCost));

        return opportunityList;
    }

    private List<LivingOpportunityDynamo> calPlaceOpportunity2(List<LivingOpportunityDynamo> livingOpportunities, int workDays) {
        // PaginatedList를 ArrayList로 복사
        List<LivingOpportunityDynamo> opportunityList = new ArrayList<>(livingOpportunities);

        for (LivingOpportunityDynamo opportunity : opportunityList) {
            // commuteCost 계산 및 할당
            int commuteCost = (int) (opportunity.getCommuteCost() * ((double) workDays / 5));
            opportunity.setCommuteCost(commuteCost);

            // totalOpportunityCost 계산 및 할당
            int totalOpportunityCost = commuteCost + opportunity.getRentCost();
            opportunity.setTotalOpportunityCost(totalOpportunityCost);
        }

        // totalOpportunityCost를 기준으로 정렬
        opportunityList.sort(Comparator.comparingInt(LivingOpportunityDynamo::getTotalOpportunityCost));

        return opportunityList;
    }
}
