package com.enm.whereToLive.Service.dabang;

import com.enm.whereToLive.Data.dabang.DabangResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class DabangApiClient {

    private UtilService utilService;

    private WebClient webClient;

    @Autowired
    public DabangApiClient(UtilService utilService) {
        this.utilService = utilService;

        this.webClient = WebClient.builder()
                .filter(logRequest())
                //.baseUrl("https://www.dabangapp.com")
                //.defaultHeader("Host", "www.dabangapp.com")
                //.defaultHeader("accept", "application/json, text/plain, */*")
//                .defaultHeader("Accept-Encoding", "gzip, deflate, br, zstd")
//                .defaultHeader("Accept-Language", "ko,en;q=0.9,en-US;q=0.8")
//                .defaultHeader("Cache-Control", "no-cache")
//                .defaultHeader("Csrf", "token")
                .defaultHeader("d-api-version", "5.0.0")
//                .defaultHeader("D-App-Version", "1")
                .defaultHeader("d-call-type", "web")
//                .defaultHeader("Expires", "-1")
//                .defaultHeader("Pragma", "no-cache")
//                .defaultHeader("Priority", "u=1, i")
//                .defaultHeader("Referer", "https://www.dabangapp.com/map/onetwo?m_lat=37.489480512688885&m_lng=126.72452479600906&m_zoom=15&search_type=subway&search_id=330")
//                .defaultHeader("Sec-Ch-Ua", "\"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Microsoft Edge\";v=\"126\"")
//                .defaultHeader("Sec-Ch-Ua-Mobile", "?0")
//                .defaultHeader("Sec-Ch-Ua-Platform", "\"Windows\"")
//                .defaultHeader("Sec-Fetch-Dest", "empty")
//                .defaultHeader("Sec-Fetch-Mode", "cors")
//                .defaultHeader("Sec-Fetch-Site", "same-origin")
                //.defaultHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36 Edg/126.0.0.0")
                .build();
    }

    public DabangResponse getSubwayStationRentalInfo(double lat, double lng) throws UnsupportedEncodingException {
        //String url = "/api/v5/markers/category/one-two?bbox=%7B%22sw%22%3A%7B%22lat%22%3A37.4816141%2C%22lng%22%3A126.7073801%7D%2C%22ne%22%3A%7B%22lat%22%3A37.4973461%2C%22lng%22%3A126.7416695%7D%7D&filters=%7B%22sellingTypeList%22%3A%5B%22MONTHLY_RENT%22%2C%22LEASE%22%5D%2C%22depositRange%22%3A%7B%22min%22%3A0%2C%22max%22%3A999999%7D%2C%22priceRange%22%3A%7B%22min%22%3A0%2C%22max%22%3A999999%7D%2C%22isIncludeMaintenance%22%3Afalse%2C%22pyeongRange%22%3A%7B%22min%22%3A0%2C%22max%22%3A999999%7D%2C%22useApprovalDateRange%22%3A%7B%22min%22%3A0%2C%22max%22%3A999999%7D%2C%22roomFloorList%22%3A%5B%22GROUND_FIRST%22%2C%22GROUND_SECOND_OVER%22%2C%22SEMI_BASEMENT%22%2C%22ROOFTOP%22%5D%2C%22roomTypeList%22%3A%5B%22ONE_ROOM%22%2C%22TWO_ROOM%22%5D%2C%22dealTypeList%22%3A%5B%22AGENT%22%2C%22DIRECT%22%5D%2C%22canParking%22%3Afalse%2C%22isShortLease%22%3Afalse%2C%22hasElevator%22%3Afalse%2C%22hasPano%22%3Afalse%2C%22isDivision%22%3Afalse%2C%22isDuplex%22%3Afalse%7D&useMap=naver&zoom=15";


        //성공
//        String baseUrl = "https://www.dabangapp.com";
//        String path = "/api/v5/markers/category/one-two";
//        String queryParams = "bbox=%7B%22sw%22%3A%7B%22lat%22%3A37.4816141%2C%22lng%22%3A126.7073801%7D%2C%22ne%22%3A%7B%22lat%22%3A37.4973461%2C%22lng%22%3A126.7416695%7D%7D&filters=%7B%22sellingTypeList%22%3A%5B%22MONTHLY_RENT%22%2C%22LEASE%22%5D%2C%22depositRange%22%3A%7B%22min%22%3A0%2C%22max%22%3A999999%7D%2C%22priceRange%22%3A%7B%22min%22%3A0%2C%22max%22%3A999999%7D%2C%22isIncludeMaintenance%22%3Afalse%2C%22pyeongRange%22%3A%7B%22min%22%3A0%2C%22max%22%3A999999%7D%2C%22useApprovalDateRange%22%3A%7B%22min%22%3A0%2C%22max%22%3A999999%7D%2C%22roomFloorList%22%3A%5B%22GROUND_FIRST%22%2C%22GROUND_SECOND_OVER%22%2C%22SEMI_BASEMENT%22%2C%22ROOFTOP%22%5D%2C%22roomTypeList%22%3A%5B%22ONE_ROOM%22%2C%22TWO_ROOM%22%5D%2C%22dealTypeList%22%3A%5B%22AGENT%22%2C%22DIRECT%22%5D%2C%22canParking%22%3Afalse%2C%22isShortLease%22%3Afalse%2C%22hasElevator%22%3Afalse%2C%22hasPano%22%3Afalse%2C%22isDivision%22%3Afalse%2C%22isDuplex%22%3Afalse%7D&useMap=naver&zoom=15";
//
//        URI uri = URI.create(baseUrl + path + "?" + queryParams);


        // 예시 "{\"sw\":{\"lat\":37.540361,\"lng\":126.955386},\"ne\":{\"lat\":37.565939,\"lng\":126.989680}}"
        String bbox = utilService.calculateBBox(lat, lng);
        String filters = "{\"sellingTypeList\":[\"MONTHLY_RENT\",\"LEASE\"],\"depositRange\":{\"min\":0,\"max\":999999},\"priceRange\":{\"min\":0,\"max\":999999},\"isIncludeMaintenance\":false,\"pyeongRange\":{\"min\":0,\"max\":999999},\"useApprovalDateRange\":{\"min\":0,\"max\":999999},\"roomFloorList\":[\"GROUND_FIRST\",\"GROUND_SECOND_OVER\",\"SEMI_BASEMENT\",\"ROOFTOP\"],\"roomTypeList\":[\"ONE_ROOM\",\"TWO_ROOM\"],\"dealTypeList\":[\"AGENT\",\"DIRECT\"],\"canParking\":false,\"isShortLease\":false,\"hasElevator\":false,\"hasPano\":false,\"isDivision\":false,\"isDuplex\":false}";

        // URI 생성
        URI uri = UriComponentsBuilder.fromUriString("https://www.dabangapp.com/api/v5/markers/category/one-two")
                .queryParam("bbox",  URLEncoder.encode(bbox, StandardCharsets.UTF_8.toString()))
                .queryParam("filters", URLEncoder.encode(filters, StandardCharsets.UTF_8.toString()))
                .queryParam("useMap", "naver")
                .queryParam("zoom", "15")
                .build(true)
                .toUri();


        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(DabangResponse.class)
                .block();

    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request: " + clientRequest.method() + " " + clientRequest.url());
            clientRequest.headers().forEach((name, values) ->
                    values.forEach(value -> System.out.println(name + ": " + value)));
            return Mono.just(clientRequest);
        });
    }
}
