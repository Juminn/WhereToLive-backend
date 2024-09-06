package com.enm.whereToLive.Data.dabang;

import lombok.Data;

import java.util.List;


@Data
public class SubwayStation {
    private String name;
    private List<AveragePrice> averagePriceList;

}
