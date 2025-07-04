package com.enm.whereToLive.dto;

import com.enm.whereToLive.entity.LivingOpportunityEntityMySQL;
import com.enm.whereToLive.model.Destination;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpportunityResponseDTO {

    @NotNull(message = "도착지는 필수입니다.")
    Destination destination;

    @NotNull(message = "기회비용은 필수입니다.")
    List<LivingOpportunityEntityMySQL> livingOpportunities;
}
