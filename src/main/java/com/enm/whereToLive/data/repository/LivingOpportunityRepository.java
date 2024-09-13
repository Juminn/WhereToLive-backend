package com.enm.whereToLive.data.repository;

import com.enm.whereToLive.data.entity.LivingOpportunity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface LivingOpportunityRepository extends CrudRepository<LivingOpportunity, String> {
}