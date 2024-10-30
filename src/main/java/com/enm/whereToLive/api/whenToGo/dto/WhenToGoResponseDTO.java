package com.enm.whereToLive.api.whenToGo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhenToGoResponseDTO {
    int minCost;
    int minDuration;
}
