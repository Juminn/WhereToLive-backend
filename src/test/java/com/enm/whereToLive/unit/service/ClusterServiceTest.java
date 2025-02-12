package com.enm.whereToLive.unit.service;

import com.enm.whereToLive.entity.ClusterEntity;
import com.enm.whereToLive.exception.ClusterNotFoundException;
import com.enm.whereToLive.repository.mysql.ClusterRepository;
import com.enm.whereToLive.service.ClusterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("단위테스트::서비스::ClusterService")
class ClusterServiceTest {

    @InjectMocks
    ClusterService clusterService;

    @Mock
    ClusterRepository clusterRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("findClusterIdByCoordinates::정상케이스")
    public void findClusterIdByCoordinates_ExistingCluster() throws ClusterNotFoundException {
        // Given
        double latitude = 37.5665; // 예시 위도
        double longitude = 126.978; // 예시 경도
        String expectedClusterId = "3-0"; // 예시 클러스터 ID

        // 클러스터가 존재한다고 가정
        when(clusterRepository.findByIdAndStatusNot("3", ClusterEntity.Status.PENDING)).thenReturn(Optional.of(new ClusterEntity()));
        when(clusterRepository.findByIdAndStatusNot("3-0", ClusterEntity.Status.PENDING)).thenReturn(Optional.of(new ClusterEntity()));

        // When
        String actualClusterId = clusterService.findClusterIdByCoordinates(latitude, longitude);

        // Then
        assertEquals(expectedClusterId, actualClusterId);
    }

    @Test
    @DisplayName("findClusterIdByCoordinates::clusterRepository 조회 null 케이스::Cluster 테이블")
    public void findClusterIdByCoordinates_NonExistingCluster() {
        // Given
        double latitude = 37.5665; // 예시 위도
        double longitude = 126.978; // 예시 경도

        when(clusterRepository.findByIdAndStatusNot("3", ClusterEntity.Status.PENDING)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ClusterNotFoundException.class, () -> {
            clusterService.findClusterIdByCoordinates(latitude, longitude);
        });
    }

    @Test
    void initializeClusters() {
    }

    @Test
    void generateDailyClusters() {
    }

    @Test
    void findClusterByCoordinates() {
    }

    @Test
    void getOpportunityCostByCoordinates() {
    }
}