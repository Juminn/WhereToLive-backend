package com.enm.whereToLive.unit.dto;

import com.enm.whereToLive.dto.GoingWorkDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GoingWorkDTOTest {

    @Test
    public void create() {
        // Given
        GoingWorkDTO goingWorkDTO = GoingWorkDTO.builder()
                .cost(1000)
                .duration(30)
                .build();

        // When
        // Then
        assertThat(goingWorkDTO.getCost()).isEqualTo(1000);
        assertThat(goingWorkDTO.getDuration()).isEqualTo(30);
    }

    @Test
    public void allArgsConstructor() {
        // Given
        int cost = 1000;
        int duration = 30;

        // When
        GoingWorkDTO dto = new GoingWorkDTO(cost, duration);

        // Then
        assertThat(dto.getCost()).isEqualTo(cost);
        assertThat(dto.getDuration()).isEqualTo(duration);
    }

    @Test
    public void noArgsConstructorAndSetters() {
        // Given
        GoingWorkDTO dto = new GoingWorkDTO();

        // When
        dto.setCost(2000);
        dto.setDuration(45);

        // Then
        assertThat(dto.getCost()).isEqualTo(2000);
        assertThat(dto.getDuration()).isEqualTo(45);
    }

    @Test
    public void ToString() {
        // Given
        GoingWorkDTO dto = new GoingWorkDTO(1000, 30);

        // When
        String result = dto.toString();

        // Then
        assertThat(result).contains("cost=1000", "duration=30");
    }

}