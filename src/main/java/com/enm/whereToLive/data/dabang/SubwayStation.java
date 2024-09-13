package com.enm.whereToLive.data.dabang;

import lombok.Data;

import java.util.List;


@Data
public class SubwayStation {
    private String name;
    private List<AveragePrice> averagePriceList;

}
