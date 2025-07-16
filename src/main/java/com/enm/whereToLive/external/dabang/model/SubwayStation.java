package com.enm.whereToLive.external.dabang.model;

import lombok.Data;

import java.util.List;


@Data
public class SubwayStation {
    private String name;
    private List<AveragePrice> averagePriceList;

}
