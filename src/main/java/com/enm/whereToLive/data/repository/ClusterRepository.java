package com.enm.whereToLive.data.repository;

import com.enm.whereToLive.data.cluster.Cluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long> {

    // 현재 분할된 클러스터 중 최대 level 반환
    @Query("SELECT MAX(c.level) FROM Cluster c")
    Integer findMaxLevel();

    // 오늘 생성된 클러스터 가져오기
    @Query("SELECT c FROM Cluster c WHERE c.createdAt >= CURRENT_DATE")
    List<Cluster> findClustersCreatedToday();
}
