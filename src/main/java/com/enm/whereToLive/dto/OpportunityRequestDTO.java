package com.enm.whereToLive.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityRequestDTO {

    @NotNull(message = "위도 필수")
    Double latitude;

    @NotNull(message = "경도 필수")
    Double longitude;

    @Min(value = 0, message = "근무일수 최소 0일")
    @Max(value = 7, message = "근무일수 최대 7일")
    int workdays;
}
