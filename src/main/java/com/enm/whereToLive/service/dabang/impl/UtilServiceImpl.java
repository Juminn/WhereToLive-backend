package com.enm.whereToLive.service.dabang.impl;

import com.enm.whereToLive.service.dabang.UtilService;
import org.springframework.stereotype.Service;

@Service
public class UtilServiceImpl implements UtilService {

    private static final double LATITUDE_CHANGE = 0.012789;
    private static final double LONGITUDE_CHANGE = 0.017147;

    public String calculateBBox(double originalLat, double originalLng) {
        double swLat = originalLat - LATITUDE_CHANGE;
        double swLng = originalLng - LONGITUDE_CHANGE;
        double neLat = originalLat + LATITUDE_CHANGE;
        double neLng = originalLng + LONGITUDE_CHANGE;

        return String.format("{\"sw\":{\"lat\":%.6f,\"lng\":%.6f},\"ne\":{\"lat\":%.6f,\"lng\":%.6f}}",
                swLat, swLng, neLat, neLng);
    }
}
