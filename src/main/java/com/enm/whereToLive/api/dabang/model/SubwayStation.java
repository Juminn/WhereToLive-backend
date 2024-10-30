package com.enm.whereToLive.api.dabang.model;

import lombok.Data;

import java.util.List;


@Data
public class SubwayStation {
    private String name;
    private List<AveragePrice> averagePriceList;

}
