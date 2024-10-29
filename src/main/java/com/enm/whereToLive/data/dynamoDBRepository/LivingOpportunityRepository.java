package com.enm.whereToLive.data.dynamoDBRepository;

import com.enm.whereToLive.data.entity.LivingOpportunityDynamo;
import com.enm.whereToLive.data.entity.LivingOpportunityDynamoID;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface LivingOpportunityRepository extends CrudRepository<LivingOpportunityDynamo, LivingOpportunityDynamoID> {
    List<LivingOpportunityDynamo> findByDestination(String destination);
}
