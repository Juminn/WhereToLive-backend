package com.enm.whereToLive.Service.dabang;

import com.enm.whereToLive.Data.Station;
import com.enm.whereToLive.Data.dabang.AveragePrice;
import com.enm.whereToLive.Data.dabang.DabangResponse;
import com.enm.whereToLive.Data.dabang.Rental;
import com.enm.whereToLive.Data.dabang.SubwayStation;
import org.springframework.stereotype.Service;

@Service
public class DabangService {
    DabangApiClient dabangAPIClient;

    public DabangService(DabangApiClient dabangAPIClient){
        this.dabangAPIClient = dabangAPIClient;
    }

    public Double getSubwayStationRental(Station station, String category, String roomType) throws Exception {
        DabangResponse response = dabangAPIClient.getSubwayStationRentalInfo(station.getLatitude(), station.getLongitude());
        String strSubwayRent = extractMonthlyRent(response, station,category, roomType);

        if(strSubwayRent == null){
            return null;
        }

        Rental subwayRent = parseRental(strSubwayRent);

        return subwayRent.evaluateMonthlyRent();
    }

    private String extractMonthlyRent(DabangResponse response, Station targetStation, String category, String roomType) throws Exception {

        String stationName = targetStation.getName();
        String monthlyRent = null;

        if (response.getResult() == null || response.getSubwayList() == null) {
            return null;
            //throw new Exception("extractMonthlyRent: No data available");
        }

        for (SubwayStation station : response.getSubwayList()) {
            if (station.getName().equals(stationName)) {
                for (AveragePrice price : station.getAveragePriceList()) {
                    if (price.getBuildingCategory().equals(category) && price.getRoomType().equals(roomType)) {
                        System.out.println(station);
                        monthlyRent = price.getMonthlyRent();
                    }
                }
            }
        }

        if(monthlyRent == null){
            return null;
            //throw new Exception("extractMonthlyRent: Not found");
        }
        else{
            return monthlyRent;
        }

    }

    /**
     * 문자열 입력을 받아 보증금과 월세를 Rental타입으로 변환 후 리턴
     * @param rentalInput "보증금/월세" 형태의 문자열
     * @return Rental 첫 번째 요소는 보증금, 두 번째 요소는 월세
     */
    public Rental parseRental(String rentalInput) {
        if(rentalInput == null || !rentalInput.contains("/")){

            return null;
        }

        String[] parts = rentalInput.split("/");

        parts[0] = parts[0].replace("억", "0");

        int deposit = Integer.parseInt(parts[0].trim());
        int monthlyRent = Integer.parseInt(parts[1].trim());

        return new Rental(deposit, monthlyRent);
    }
}
