package com.enm.whereToLive.service;

//import com.example.seoulclusters.model.Cluster;
//import com.example.seoulclusters.model.NotFoundException;
//import com.example.seoulclusters.repository.ClusterRepository;
import com.amazonaws.services.kms.model.NotFoundException;
import com.enm.whereToLive.entity.ClusterEntity;
import com.enm.whereToLive.repository.mysql.ClusterRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClusterService {

    @Autowired
    private ClusterRepository clusterRepository;

    private static final int MAX_LEVEL = 10;

    private static final Logger logger = LoggerFactory.getLogger(ClusterService.class);

    // 서울의 위도와 경도 범위
    private static final double LAT_MIN = 37.413294;
    private static final double LAT_MAX = 37.715133;
    private static final double LON_MIN = 126.734086;
    private static final double LON_MAX = 127.183887;

    // 분할 대기 클러스터 큐
    private Queue<ClusterEntity> clusterEntityQueue = new LinkedList<>();

    // 초기화 메소드
    @PostConstruct
    public void initializeClusters() {
        if (clusterRepository.count() == 0) {
            generateInitialClusters();
        }

        List<ClusterEntity> pendingClusterEntities = clusterRepository.findByStatusOrderByLevelAsc(ClusterEntity.Status.PENDING);
        //clusterQueue.addAll(pendingClusters);
    }

    //@PostConstruct
    public void test() {
        //1-3-1-0-2 37.49818621875,127.127661875 non split
        getOpportunityCostByCoordinates(37.49818621875,127.127661875);
        getOpportunityCostByCoordinates(37.49818621876,127.127661876);

        //0-0-2-0  37.451023875000004,126.734086 split
        getOpportunityCostByCoordinates(37.451023875000004,126.734086);
        getOpportunityCostByCoordinates(37.451023875000005,126.734087);
    }

    // 초기 클러스터 생성
    private void generateInitialClusters() {
        int gridSize = 2; // 4x4 그리드
        double latStep = (LAT_MAX - LAT_MIN) / gridSize;
        double lonStep = (LON_MAX - LON_MIN) / gridSize;

        List<ClusterEntity> clusterEntities = new ArrayList<>();

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                double minLat = LAT_MIN + row * latStep;
                double maxLat = minLat + latStep;
                double minLon = LON_MIN + col * lonStep;
                double maxLon = minLon + lonStep;

                String clusterId = generateClusterId(null, row * gridSize + col);
                //long clusterId = computeMortonCode(row, col, 2); // 2비트씩 사용 (4x4 그리드)
                //clusterId = 1;

                ClusterEntity clusterEntity = new ClusterEntity();
                clusterEntity.setId(clusterId);
                clusterEntity.setLevel(0);
                clusterEntity.setMinLatitude(minLat);
                clusterEntity.setMaxLatitude(maxLat);
                clusterEntity.setMinLongitude(minLon);
                clusterEntity.setMaxLongitude(maxLon);
                clusterEntity.setStatus(ClusterEntity.Status.PENDING);
                clusterEntity.setCreatedAt(LocalDateTime.now());

                clusterEntities.add(clusterEntity);
            }
        }

        clusterRepository.saveAll(clusterEntities);

        // 분할 큐에 초기 클러스터 추가
        //clusterQueue.addAll(clusters);
    }

    private String generateClusterId(String parentId, int index) {
        if (parentId == null || parentId.isEmpty()) {
            return String.valueOf(index);
        } else {
            return parentId + "-" + index;
        }
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
    public void generateDailyClusters() {
        List<ClusterEntity> newClusterEntities = new ArrayList<>();

        Optional<ClusterEntity> optionalParentCluster = clusterRepository.findFirstByStatusOrderByLevelAsc(ClusterEntity.Status.CAL_COMPLETED);
        ClusterEntity parentClusterEntity;

        if (optionalParentCluster.isEmpty()){
            logger.error("No CAL_COMPLETED Cluster");
            return;
        }
        else {
            parentClusterEntity = optionalParentCluster.get();
        }

        // 클러스터 분할
        List<ClusterEntity> subClusterEntities = splitCluster(parentClusterEntity);
        newClusterEntities.addAll(subClusterEntities);

        // 새로운 클러스터를 저장
        clusterRepository.saveAll(newClusterEntities);

        // 부모 클러스터
        parentClusterEntity.setStatus(ClusterEntity.Status.SPLIT_COMPLETED);
        clusterRepository.save(parentClusterEntity);
    }

    // 최대 분할 수준 설정 (필요에 따라 조정)
    private int getMaxLevel() {
        return 5; // 예시로 최대 5단계까지 분할
    }

    // 클러스터 분할 메소드
    private List<ClusterEntity> splitCluster(ClusterEntity parentClusterEntity) {
        List<ClusterEntity> subClusterEntities = new ArrayList<>();

        double minLat = parentClusterEntity.getMinLatitude();
        double maxLat = parentClusterEntity.getMaxLatitude();
        double minLon = parentClusterEntity.getMinLongitude();
        double maxLon = parentClusterEntity.getMaxLongitude();

        double midLat = (minLat + maxLat) / 2;
        double midLon = (minLon + maxLon) / 2;

        int nextLevel = parentClusterEntity.getLevel() + 1;

        for (int i = 0; i < 4; i++) {
            double subMinLat = (i / 2 == 0) ? minLat : midLat;
            double subMaxLat = (i / 2 == 0) ? midLat : maxLat;
            double subMinLon = (i % 2 == 0) ? minLon : midLon;
            double subMaxLon = (i % 2 == 0) ? midLon : maxLon;

            String subClusterId = generateClusterId(parentClusterEntity.getId(), i);
            //long subClusterId = (parentCluster.getId() << 2) | i;


            // 자식 클러스터의 상대적인 위치 (x, y)
            int x = i % 2;
            int y = i / 2;
            // 자식 클러스터의 Morton 코드 계산
            long subMortonCode = computeMortonCode(x, y, 1);
            // 부모 클러스터의 ID를 왼쪽으로 2비트 시프트하고 자식의 Morton 코드 추가
             //subClusterId = (parentCluster.getId() << 2) | subMortonCode;

            ClusterEntity subClusterEntity = new ClusterEntity();
            subClusterEntity.setId(subClusterId);
            subClusterEntity.setLevel(nextLevel);
            subClusterEntity.setMinLatitude(subMinLat);
            subClusterEntity.setMaxLatitude(subMaxLat);
            subClusterEntity.setMinLongitude(subMinLon);
            subClusterEntity.setMaxLongitude(subMaxLon);
            subClusterEntity.setStatus(ClusterEntity.Status.PENDING);
            subClusterEntity.setParentId(parentClusterEntity.getId());
            subClusterEntity.setCreatedAt(LocalDateTime.now());

            subClusterEntities.add(subClusterEntity);
        }

        return subClusterEntities;
    }

    //좌표로 클러스터 찾기
    public ClusterEntity findClusterByCoordinates(double latitude, double longitude) {
        String clusterId = findClusterIdByCoordinates(latitude, longitude);
        ClusterEntity clusterEntity = clusterRepository.findById(clusterId).orElse(null);
        if (clusterEntity != null) {
            return clusterEntity;
        } else {
            logger.error("해당 좌표에 대한 클러스터를 찾을 수 없습니다.");
            throw new NotFoundException("해당 좌표에 대한 클러스터를 찾을 수 없습니다.");
        }

    }

    // 좌표로 클러스터 ID찾기
    public String findClusterIdByCoordinates(double latitude, double longitude) {
//        long clusterId = 0;
        String clusterId = "";
        int level = 0;

        double minLat = LAT_MIN;
        double maxLat = LAT_MAX;
        double minLon = LON_MIN;
        double maxLon = LON_MAX;

        //Cluster cluster = null;

        while (level <= MAX_LEVEL) {
            double midLat = (minLat + maxLat) / 2;
            double midLon = (minLon + maxLon) / 2;

            int rowOffset = (latitude >= midLat) ? 1 : 0;
            int colOffset = (longitude >= midLon) ? 1 : 0;

            int index = rowOffset * 2 + colOffset;

            clusterId = generateClusterId(clusterId, index);

            Optional<ClusterEntity> clusterOpt = clusterRepository.findByIdAndStatusNot(clusterId, ClusterEntity.Status.PENDING);
            if (clusterOpt.isEmpty()) {
                // 존재하지 않으면 이전 레벨의 클러스터 ID 반환
                return clusterId.substring(0, clusterId.lastIndexOf('-'));
            }

            if (level == MAX_LEVEL) {
                break;
            }

            //경계 업데이트
            if (rowOffset == 0) {
                maxLat =midLat;
            } else {
                minLat = midLat;
            }
            if (colOffset == 0) {
                maxLon = midLon;
            } else {
                minLon = midLon;
            }

            level++;

//            int quadrant = 0;
//            if (latitude >= midLat) {
//                quadrant |= 2; // 위쪽
//                minLat = midLat;
//            } else {
//                maxLat = midLat;
//            }
//
//            if (longitude >= midLon) {
//                quadrant |= 1; // 오른쪽
//                minLon = midLon;
//            } else {
//                maxLon = midLon;
//            }
//
//            clusterId = (clusterId << 2) | quadrant;
//            level++;
//
//            // 클러스터 조회
//            cluster = clusterRepository.findById(clusterId).orElse(null);
//
//            if (cluster != null) {
//                if (cluster.getLevel() == level) {
//                    // 가장 상세한 클러스터를 찾음
//                    return cluster;
//                }
//            } else {
//                // 클러스터가 아직 생성되지 않았으면 상위 클러스터 반환
//                clusterId >>= 2;
//                level--;
//                return clusterRepository.findById(clusterId).orElse(null);
//            }
//
//            // 최대 수준에 도달하면 종료
//            if (level >= getCurrentMaxLevel()) {
//                break;
//            }
        }

        return clusterId;
    }

    // 현재 최대 분할 수준 반환
    private int getCurrentMaxLevel() {
        Integer maxLevel = clusterRepository.findMaxLevel();
        return maxLevel != null ? maxLevel : 0;
    }

    // 좌표를 기반으로 기회 비용을 조회하는 메소드
    public double getOpportunityCostByCoordinates(double latitude, double longitude) {
        ClusterEntity clusterEntity = findClusterByCoordinates(latitude, longitude);
        if (clusterEntity != null) {
            logger.info(clusterEntity.toString());
            return 0;
        } else {
            throw new RuntimeException("해당 좌표에 대한 기회 비용을 찾을 수 없습니다.");
        }
    }
}
