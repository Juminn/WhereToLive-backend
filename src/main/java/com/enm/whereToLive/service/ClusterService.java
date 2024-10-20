package com.enm.whereToLive.service;

//import com.example.seoulclusters.model.Cluster;
//import com.example.seoulclusters.model.NotFoundException;
//import com.example.seoulclusters.repository.ClusterRepository;
import com.enm.whereToLive.data.cluster.Cluster;
import com.enm.whereToLive.data.repository.ClusterRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClusterService {

    @Autowired
    private ClusterRepository clusterRepository;

    // 서울의 위도와 경도 범위
    private static final double LAT_MIN = 37.413294;
    private static final double LAT_MAX = 37.715133;
    private static final double LON_MIN = 126.734086;
    private static final double LON_MAX = 127.183887;

    // 분할 대기 클러스터 큐
    private Queue<Cluster> clusterQueue = new LinkedList<>();

    // 초기화 메소드
    @PostConstruct
    public void initializeClusters() {
        if (clusterRepository.count() == 0) {
            generateInitialClusters();
        }
    }

    // 초기 클러스터 생성
    private void generateInitialClusters() {
        int gridSize = 4; // 4x4 그리드
        double latStep = (LAT_MAX - LAT_MIN) / gridSize;
        double lonStep = (LON_MAX - LON_MIN) / gridSize;

        List<Cluster> clusters = new ArrayList<>();

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                double minLat = LAT_MIN + row * latStep;
                double maxLat = minLat + latStep;
                double minLon = LON_MIN + col * lonStep;
                double maxLon = minLon + lonStep;

                long clusterId = computeMortonCode(row, col, 2); // 2비트씩 사용 (4x4 그리드)

                Cluster cluster = new Cluster();
                cluster.setId(clusterId);
                cluster.setLevel(0);
                cluster.setMinLatitude(minLat);
                cluster.setMaxLatitude(maxLat);
                cluster.setMinLongitude(minLon);
                cluster.setMaxLongitude(maxLon);
                cluster.setCreatedAt(LocalDateTime.now());

                clusters.add(cluster);
            }
        }

        clusterRepository.saveAll(clusters);

        // 분할 큐에 초기 클러스터 추가
        clusterQueue.addAll(clusters);
    }

    // Morton 코드 계산 (Z-order curve)
    private long computeMortonCode(int x, int y, int bits) {
        long mortonCode = 0;
        for (int i = 0; i < bits; i++) {
            mortonCode |= ((x >> i) & 1L) << (2 * i + 1);
            mortonCode |= ((y >> i) & 1L) << (2 * i);
        }
        return mortonCode;
    }

    // 매일 호출되는 클러스터 분할 메소드
    public void generateDailyClusters(int dailyLimit) {
        List<Cluster> newClusters = new ArrayList<>();

        int clustersToProcess = dailyLimit;
        while (clustersToProcess > 0 && !clusterQueue.isEmpty()) {
            Cluster parentCluster = clusterQueue.poll();

            // 최대 수준에 도달했으면 건너뜁니다.
            if (parentCluster.getLevel() >= getMaxLevel()) {
                continue;
            }

            // 클러스터 분할
            List<Cluster> subClusters = splitCluster(parentCluster);
            newClusters.addAll(subClusters);

            // 분할된 클러스터를 큐에 추가
            clusterQueue.addAll(subClusters);

            clustersToProcess--;
        }

        // 새로운 클러스터를 저장
        clusterRepository.saveAll(newClusters);
    }

    // 최대 분할 수준 설정 (필요에 따라 조정)
    private int getMaxLevel() {
        return 5; // 예시로 최대 5단계까지 분할
    }

    // 클러스터 분할 메소드
    private List<Cluster> splitCluster(Cluster parentCluster) {
        List<Cluster> subClusters = new ArrayList<>();

        double minLat = parentCluster.getMinLatitude();
        double maxLat = parentCluster.getMaxLatitude();
        double minLon = parentCluster.getMinLongitude();
        double maxLon = parentCluster.getMaxLongitude();

        double midLat = (minLat + maxLat) / 2;
        double midLon = (minLon + maxLon) / 2;

        int nextLevel = parentCluster.getLevel() + 1;

        for (int i = 0; i < 4; i++) {
            double subMinLat = (i / 2 == 0) ? minLat : midLat;
            double subMaxLat = (i / 2 == 0) ? midLat : maxLat;
            double subMinLon = (i % 2 == 0) ? minLon : midLon;
            double subMaxLon = (i % 2 == 0) ? midLon : maxLon;

            long subClusterId = (parentCluster.getId() << 2) | i;

            Cluster subCluster = new Cluster();
            subCluster.setId(subClusterId);
            subCluster.setLevel(nextLevel);
            subCluster.setMinLatitude(subMinLat);
            subCluster.setMaxLatitude(subMaxLat);
            subCluster.setMinLongitude(subMinLon);
            subCluster.setMaxLongitude(subMaxLon);
            subCluster.setParentId(parentCluster.getId());
            subCluster.setCreatedAt(LocalDateTime.now());

            subClusters.add(subCluster);
        }

        return subClusters;
    }

    // 좌표로 클러스터 찾기
    public Cluster findClusterByCoordinates(double latitude, double longitude) {
        long clusterId = 0;
        int level = 0;

        double minLat = LAT_MIN;
        double maxLat = LAT_MAX;
        double minLon = LON_MIN;
        double maxLon = LON_MAX;

        Cluster cluster = null;

        while (true) {
            double midLat = (minLat + maxLat) / 2;
            double midLon = (minLon + maxLon) / 2;

            int quadrant = 0;
            if (latitude >= midLat) {
                quadrant |= 2; // 위쪽
                minLat = midLat;
            } else {
                maxLat = midLat;
            }

            if (longitude >= midLon) {
                quadrant |= 1; // 오른쪽
                minLon = midLon;
            } else {
                maxLon = midLon;
            }

            clusterId = (clusterId << 2) | quadrant;
            level++;

            // 클러스터 조회
            cluster = clusterRepository.findById(clusterId).orElse(null);

            if (cluster != null) {
                if (cluster.getLevel() == level) {
                    // 가장 상세한 클러스터를 찾음
                    return cluster;
                }
            } else {
                // 클러스터가 아직 생성되지 않았으면 상위 클러스터 반환
                clusterId >>= 2;
                level--;
                return clusterRepository.findById(clusterId).orElse(null);
            }

            // 최대 수준에 도달하면 종료
            if (level >= getCurrentMaxLevel()) {
                break;
            }
        }

        return cluster;
    }

    // 현재 최대 분할 수준 반환
    private int getCurrentMaxLevel() {
        Integer maxLevel = clusterRepository.findMaxLevel();
        return maxLevel != null ? maxLevel : 0;
    }

    // 좌표를 기반으로 기회 비용을 조회하는 메소드
    public double getOpportunityCostByCoordinates(double latitude, double longitude) {
        Cluster cluster = findClusterByCoordinates(latitude, longitude);
        if (cluster != null && cluster.getOpportunityCost() != null) {
            return cluster.getOpportunityCost();
        } else {
            throw new RuntimeException("해당 좌표에 대한 기회 비용을 찾을 수 없습니다.");
        }
    }
}
