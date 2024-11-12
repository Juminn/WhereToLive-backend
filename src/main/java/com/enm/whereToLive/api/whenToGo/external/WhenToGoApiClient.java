package com.enm.whereToLive.api.whenToGo.external;

import com.enm.whereToLive.api.whenToGo.dto.WhenToGoRequestDTO;
import com.enm.whereToLive.api.whenToGo.dto.WhenToGoResponseDTO;
import com.enm.whereToLive.api.dabang.service.UtilService;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
@Slf4j
public class WhenToGoApiClient {

    private WebClient webClient;

    public WhenToGoApiClient(UtilService utilService) {

        //test용 SSL인증 비활성화
        HttpClient httpClient = HttpClient.create()
                .secure(t -> t.sslContext(SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)));

        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(logRequest())
                .baseUrl("https://api.xn--ih3bt9oq0b6yi50k.com")
                .build();
    }

    //https://api.xn--ih3bt9oq0b6yi50k.com/a?startX=127.1022736&startY=37.3657395&goalX=127.1260058&goalY=37.3642729&startTime=2024-02-05T09:00&endTime=2024-02-05T09:30&transferCost=4000&subwayCost=10000&busCost=14000&walkingCost=20000
    public WhenToGoResponseDTO getGoingOpportunity(WhenToGoRequestDTO whenToGoRequestDTO) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/a")
                        .queryParam("startX", whenToGoRequestDTO.getStartX())
                        .queryParam("startY", whenToGoRequestDTO.getStartY())
                        .queryParam("goalX", whenToGoRequestDTO.getGoalX())
                        .queryParam("goalY", whenToGoRequestDTO.getGoalY())
                        .queryParam("startTime", whenToGoRequestDTO.getStartTime())
                        .queryParam("endTime", whenToGoRequestDTO.getEndTime())
                        .queryParam("subwayCost", whenToGoRequestDTO.getSubwayCost())
                        .queryParam("busCost", whenToGoRequestDTO.getBusCost())
                        .queryParam("walkingCost", whenToGoRequestDTO.getWalkingCost())
                        .queryParam("transferCost", whenToGoRequestDTO.getTransferCost())
                        .build())
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse -> {
                    // 에러 상태일 때 예외 처리
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), errorBody)));
                })
                .bodyToMono(WhenToGoResponseDTO.class)
                .block();

    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: " + clientRequest.method() + " " + clientRequest.url());
            clientRequest.headers().forEach((name, values) ->
                    values.forEach(value -> System.out.println(name + ": " + value)));
            return Mono.just(clientRequest);
        });
    }
}
