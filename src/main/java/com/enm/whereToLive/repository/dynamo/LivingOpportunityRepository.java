package com.enm.whereToLive.repository.dynamo;

import com.enm.whereToLive.entity.LivingOpportunityEntityDynamo;
import com.enm.whereToLive.entity.LivingOpportunityEntityDynamoID;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface LivingOpportunityRepository extends CrudRepository<LivingOpportunityEntityDynamo, LivingOpportunityEntityDynamoID> {
    List<LivingOpportunityEntityDynamo> findByDestination(String destination);
}
