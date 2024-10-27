package com.enm.whereToLive.service.impl;

import com.enm.whereToLive.data.Destination;
import com.enm.whereToLive.data.Station;
import com.enm.whereToLive.data.entity.LivingOpportunity;
import com.enm.whereToLive.data.dynamoDBRepository.LivingOpportunityRepository;
import com.enm.whereToLive.data.opportunityResponseDTO;
import com.enm.whereToLive.service.StationService;
import com.enm.whereToLive.service.WhereToLiveService;
import com.enm.whereToLive.service.dabang.DabangService;
import com.enm.whereToLive.service.whenToGo.WhenToGoService;
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

    @Autowired
    public WhereToLiveServiceImpl(StationService stationService, DabangService dabangService, com.enm.whereToLive.service.whenToGo.WhenToGoService whenToGoService, LivingOpportunityRepository livingOpportunityRepository) {
        this.stationService = stationService;
        this.dabangService = dabangService;
        this.whenToGoService = whenToGoService;
        this.livingOpportunityRepository = livingOpportunityRepository;
    }


    @Override
    public opportunityResponseDTO getPlaceOpportunity(String name, int workDays) {
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

        livingOpportunities = calPlaceOpportunity(livingOpportunities, workDays);

        opportunityResponseDTO.setLivingOpportunities(livingOpportunities);
        opportunityResponseDTO.setDestination(destination);

        return opportunityResponseDTO;
    }

    private List<LivingOpportunity> calPlaceOpportunity(List<LivingOpportunity> livingOpportunities, int workDays) {
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
