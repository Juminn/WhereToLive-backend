package com.enm.whereToLive.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "clusters")
public class ClusterEntity {

    @Id
    private String id;
    //private Long id; // 클러스터 ID (Morton 코드 사용)

    private int level; // 분할 수준 (0부터 시작)

    // 클러스터 경계
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;

    // 부모 클러스터 ID
    private String parentId;
    //private Long parentId;

    // 생성 날짜
    private LocalDateTime createdAt;

    //완료 날짜
    private LocalDateTime completeAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    // 기회 비용
    private Double opportunityCost;

    public enum Status {
        PENDING,
        PROCESSING,
        CAL_COMPLETED,
        SPLIT_COMPLETED
    }
}
