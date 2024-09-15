package com.enm.whereToLive.data.repository;

import com.enm.whereToLive.data.entity.LivingOpportunity;
import com.enm.whereToLive.data.entity.LivingOpportunityId;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface LivingOpportunityRepository extends CrudRepository<LivingOpportunity, LivingOpportunityId> {
}
