package com.enm.whereToLive.data.repository;

import com.enm.whereToLive.data.cluster.Cluster;
import com.enm.whereToLive.data.cluster.ClusterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClusterRepository extends JpaRepository<Cluster, String> {

    // 현재 분할된 클러스터 중 최대 level 반환
    @Query("SELECT MAX(c.level) FROM Cluster c")
    Integer findMaxLevel();

    // 오늘 생성된 클러스터 가져오기
    @Query("SELECT c FROM Cluster c WHERE c.createdAt >= CURRENT_DATE")
    List<Cluster> findClustersCreatedToday();

    List<Cluster> findByStatusOrderByLevelAsc(ClusterStatus status);

    Optional<Cluster> findFirstByStatus(ClusterStatus status);
    Optional<Cluster> findFirstByStatusOrderByLevelAsc(ClusterStatus status);

    Optional<Cluster> findByIdAndStatusNot(String id, ClusterStatus status);
}
