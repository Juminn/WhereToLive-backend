package com.enm.whereToLive.service.impl;

import com.enm.whereToLive.data.Destination;
import com.enm.whereToLive.data.Station;
import com.enm.whereToLive.data.cluster.Cluster;
import com.enm.whereToLive.data.cluster.LivingOpportunity2;
import com.enm.whereToLive.data.entity.LivingOpportunity;
import com.enm.whereToLive.data.dynamoDBRepository.LivingOpportunityRepository;
import com.enm.whereToLive.data.opportunityResponseDTO;
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

        List<LivingOpportunity2> livingOpportunities = livingOpportunityRepository2.findByIdDestination(cluster.getId());



        Destination destination = new Destination(cluster.getId(), livingOpportunities.get(0).getLatitude(), livingOpportunities.get(0).getLongitude());

        for(LivingOpportunity2 livingOpportunity : livingOpportunities) {
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
    public opportunityResponseDTO getPlaceOpportunity2(String name, int workDays) {
        opportunityResponseDTO opportunityResponseDTO = new opportunityResponseDTO();

        List<LivingOpportunity> livingOpportunities = livingOpportunityRepository.findByDestination(name);
        Destination destination = new Destination(name, livingOpportunities.get(0).getLatitude(), livingOpportunities.get(0).getLongitude());

        for(LivingOpportunity livingOpportunity : livingOpportunities) {
            Station station = stationService.getStationById(livingOpportunity.getStationID());

            livingOpportunity.setLatitude(station.getLatitude());
            livingOpportunity.setLongitude(station.getLongitude());
            livingOpportunity.setPros(station.getPros());
            livingOpportunity.setCons(station.getCons());
        }

        livingOpportunities = calPlaceOpportunity2(livingOpportunities, workDays);

        //opportunityResponseDTO.setLivingOpportunities(livingOpportunities);
        opportunityResponseDTO.setDestination(destination);

        return opportunityResponseDTO;
    }

    private List<LivingOpportunity2> calPlaceOpportunity(List<LivingOpportunity2> livingOpportunities, int workDays) {
        // PaginatedList를 ArrayList로 복사
        List<LivingOpportunity2> opportunityList = new ArrayList<>(livingOpportunities);

        for (LivingOpportunity2 opportunity : opportunityList) {
            // commuteCost 계산 및 할당
            int commuteCost = (int) (opportunity.getCommuteCost() * ((double) workDays / 5));
            opportunity.setCommuteCost(commuteCost);

            // totalOpportunityCost 계산 및 할당
            int totalOpportunityCost = commuteCost + opportunity.getRentCost();
            opportunity.setTotalOpportunityCost(totalOpportunityCost);
        }

        // totalOpportunityCost를 기준으로 정렬
        opportunityList.sort(Comparator.comparingInt(LivingOpportunity2::getTotalOpportunityCost));

        return opportunityList;
    }

    private List<LivingOpportunity> calPlaceOpportunity2(List<LivingOpportunity> livingOpportunities, int workDays) {
        // PaginatedList를 ArrayList로 복사
        List<LivingOpportunity> opportunityList = new ArrayList<>(livingOpportunities);

        for (LivingOpportunity opportunity : opportunityList) {
            // commuteCost 계산 및 할당
            int commuteCost = (int) (opportunity.getCommuteCost() * ((double) workDays / 5));
            opportunity.setCommuteCost(commuteCost);

            // totalOpportunityCost 계산 및 할당
            int totalOpportunityCost = commuteCost + opportunity.getRentCost();
            opportunity.setTotalOpportunityCost(totalOpportunityCost);
        }

        // totalOpportunityCost를 기준으로 정렬
        opportunityList.sort(Comparator.comparingInt(LivingOpportunity::getTotalOpportunityCost));

        return opportunityList;
    }
}
