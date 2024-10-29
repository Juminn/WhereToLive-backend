package com.enm.whereToLive.data.repository;

import com.enm.whereToLive.data.cluster.LivingOpportunityMySQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivingOpportunityRepository2 extends JpaRepository<LivingOpportunityMySQL, Long> {

    List<LivingOpportunityMySQL> findByIdDestination(String destination);
}
