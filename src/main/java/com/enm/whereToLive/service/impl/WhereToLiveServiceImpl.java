package com.enm.whereToLive.service.impl;

import com.enm.whereToLive.model.Destination;
import com.enm.whereToLive.model.Station;
import com.enm.whereToLive.entity.ClusterEntity;
import com.enm.whereToLive.entity.LivingOpportunityEntityMySQL;
import com.enm.whereToLive.entity.LivingOpportunityEntityDynamo;
import com.enm.whereToLive.repository.dynamo.LivingOpportunityRepository;
import com.enm.whereToLive.dto.opportunityResponseDTO;
import com.enm.whereToLive.dto.opportunityResponseDTO2;
import com.enm.whereToLive.repository.mysql.LivingOpportunityRepository2;
import com.enm.whereToLive.service.ClusterService;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.WhereToLiveService;
import com.enm.whereToLive.api.dabang.service.DabangService;
import com.enm.whereToLive.api.whenToGo.service.WhenToGoService;
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

        ClusterEntity clusterEntity = clusterService.findClusterByCoordinates(latitude, longitude);

        logger.info(clusterEntity.toString());

        List<LivingOpportunityEntityMySQL> livingOpportunities = livingOpportunityRepository2.findByIdDestination(clusterEntity.getId());



        Destination destination = new Destination(clusterEntity.getId(), livingOpportunities.get(0).getLatitude(), livingOpportunities.get(0).getLongitude());

        for(LivingOpportunityEntityMySQL livingOpportunity : livingOpportunities) {
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

        List<LivingOpportunityEntityDynamo> livingOpportunities = livingOpportunityRepository.findByDestination(name);
        Destination destination = new Destination(name, livingOpportunities.get(0).getLatitude(), livingOpportunities.get(0).getLongitude());

        for(LivingOpportunityEntityDynamo livingOpportunityEntityDynamo : livingOpportunities) {
            Station station = stationService.getStationById(livingOpportunityEntityDynamo.getStationID());

            livingOpportunityEntityDynamo.setLatitude(station.getLatitude());
            livingOpportunityEntityDynamo.setLongitude(station.getLongitude());
            livingOpportunityEntityDynamo.setPros(station.getPros());
            livingOpportunityEntityDynamo.setCons(station.getCons());
        }

        livingOpportunities = calPlaceOpportunity2(livingOpportunities, workDays);

        opportunityResponseDTO.setLivingOpportunities(livingOpportunities);
        opportunityResponseDTO.setDestination(destination);

        return opportunityResponseDTO;
    }

    private List<LivingOpportunityEntityMySQL> calPlaceOpportunity(List<LivingOpportunityEntityMySQL> livingOpportunities, int workDays) {
        // PaginatedList를 ArrayList로 복사
        List<LivingOpportunityEntityMySQL> opportunityList = new ArrayList<>(livingOpportunities);

        for (LivingOpportunityEntityMySQL opportunity : opportunityList) {
            // commuteCost 계산 및 할당
            int commuteCost = (int) (opportunity.getCommuteCost() * ((double) workDays / 5));
            opportunity.setCommuteCost(commuteCost);

            // totalOpportunityCost 계산 및 할당
            int totalOpportunityCost = commuteCost + opportunity.getRentCost();
            opportunity.setTotalOpportunityCost(totalOpportunityCost);
        }

        // totalOpportunityCost를 기준으로 정렬
        opportunityList.sort(Comparator.comparingInt(LivingOpportunityEntityMySQL::getTotalOpportunityCost));

        return opportunityList;
    }

    private List<LivingOpportunityEntityDynamo> calPlaceOpportunity2(List<LivingOpportunityEntityDynamo> livingOpportunities, int workDays) {
        // PaginatedList를 ArrayList로 복사
        List<LivingOpportunityEntityDynamo> opportunityList = new ArrayList<>(livingOpportunities);

        for (LivingOpportunityEntityDynamo opportunity : opportunityList) {
            // commuteCost 계산 및 할당
            int commuteCost = (int) (opportunity.getCommuteCost() * ((double) workDays / 5));
            opportunity.setCommuteCost(commuteCost);

            // totalOpportunityCost 계산 및 할당
            int totalOpportunityCost = commuteCost + opportunity.getRentCost();
            opportunity.setTotalOpportunityCost(totalOpportunityCost);
        }

        // totalOpportunityCost를 기준으로 정렬
        opportunityList.sort(Comparator.comparingInt(LivingOpportunityEntityDynamo::getTotalOpportunityCost));

        return opportunityList;
    }
}
