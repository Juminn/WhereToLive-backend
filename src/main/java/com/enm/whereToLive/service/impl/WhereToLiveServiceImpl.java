package com.enm.whereToLive.service.impl;

import com.enm.whereToLive.data.entity.LivingOpportunity;
import com.enm.whereToLive.data.dynamoDBRepository.LivingOpportunityRepository;
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
    public List<LivingOpportunity> getPlaceOpportunity(String destination, int workDays) {
        List<LivingOpportunity> livingOpportunities = livingOpportunityRepository.findByDestination(destination);
        return calPlaceOpportunity(livingOpportunities, workDays);
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
