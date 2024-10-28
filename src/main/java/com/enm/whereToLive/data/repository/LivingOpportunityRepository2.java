package com.enm.whereToLive.data.repository;

import com.enm.whereToLive.data.cluster.Cluster;
import com.enm.whereToLive.data.cluster.LivingOpportunity2;
import com.enm.whereToLive.data.entity.LivingOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivingOpportunityRepository2 extends JpaRepository<LivingOpportunity2, Long> {

    List<LivingOpportunity2> findByIdDestination(String destination);
}
