package com.enm.whereToLive.repository.mysql;

import com.enm.whereToLive.entity.LivingOpportunityEntityMySQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivingOpportunityRepository2 extends JpaRepository<LivingOpportunityEntityMySQL, Long> {

    List<LivingOpportunityEntityMySQL> findByIdDestination(String destination);
}
