package com.enm.whereToLive.data.dynamoDBRepository;

import com.enm.whereToLive.data.entity.LivingOpportunity;
import com.enm.whereToLive.data.entity.LivingOpportunityId;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface LivingOpportunityRepository extends CrudRepository<LivingOpportunity, LivingOpportunityId> {
    List<LivingOpportunity> findByDestination(String destination);
}
