package com.enm.whereToLive.repository.mysql;

import com.enm.whereToLive.entity.ClusterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClusterRepository extends JpaRepository<ClusterEntity, String> {

    // 현재 분할된 클러스터 중 최대 level 반환
    @Query("SELECT MAX(c.level) FROM ClusterEntity c")
    Integer findMaxLevel();

    // 오늘 생성된 클러스터 가져오기
    @Query("SELECT c FROM ClusterEntity c WHERE c.createdAt >= CURRENT_DATE")
    List<ClusterEntity> findClustersCreatedToday();

    List<ClusterEntity> findByStatusOrderByLevelAsc(ClusterEntity.Status status);

    Optional<ClusterEntity> findFirstByStatus(ClusterEntity.Status status);
    Optional<ClusterEntity> findFirstByStatusOrderByLevelAsc(ClusterEntity.Status status);

    Optional<ClusterEntity> findByIdAndStatusNot(String id, ClusterEntity.Status status);
}
