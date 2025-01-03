package com.enm.whereToLive.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityRequestDTO {

    double latitude;
    double longitude;
    int workdays;
}
