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
public class OpportunityRequestDTO2 {

    @NotBlank(message = "회사이름 필수")
    String company;

    @Min(value = 0, message = "근무일수 최소 0일")
    @Max(value = 7, message = "근무일수 최대 7일")
    int workdays;
}