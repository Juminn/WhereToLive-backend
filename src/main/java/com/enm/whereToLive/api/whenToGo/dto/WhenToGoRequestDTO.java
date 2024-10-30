package com.enm.whereToLive.api.whenToGo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhenToGoRequestDTO {
    double startX;
    double startY;
    double goalX;
    double goalY;
    String startTime;
    String endTime;
    int transferCost;
    int subwayCost;
    int busCost;
    int walkingCost;
}
