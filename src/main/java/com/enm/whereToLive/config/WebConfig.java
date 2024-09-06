package com.enm.whereToLive.config;

//import io.github.resilience4j.ratelimiter.RateLimiter;
//import io.github.resilience4j.ratelimiter.RateLimiterConfig;
//import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // CORS 설정
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://localhost:3000",
                        "http://localhost:3000",

                        "https://d1y6ebwgoivo5n.cloudfront.net",
                        "http://d1y6ebwgoivo5n.cloudfront.net",
                        "https://www.d1y6ebwgoivo5n.cloudfront.net",
                        "http://www.d1y6ebwgoivo5n.cloudfront.net",

                        "https://xn--ih3bt9oq0b6yi50k.com",
                        "http://xn--ih3bt9oq0b6yi50k.com",
                        "https://www.xn--ih3bt9oq0b6yi50k.com",
                        "http://www.xn--ih3bt9oq0b6yi50k.com",

                        "https://xn--w39a06bmycv8qzokvne.com",
                        "http://xn--w39a06bmycv8qzokvne.com",
                        "https://www.xn--w39a06bmycv8qzokvne.com",
                        "http://www.xn--w39a06bmycv8qzokvne.com"
                );
    }

//    @Bean
//    public RateLimiter rateLimiter() {
//        RateLimiterConfig config = RateLimiterConfig.custom()
//                .limitForPeriod(1) //
//                .limitRefreshPeriod(Duration.ofSeconds(40)) // 1회 요청주기 40초
//                .timeoutDuration(Duration.ofSeconds(1200)) // 요청 타임아웃 기간
//                .build();
//
//        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
//        return registry.rateLimiter("naverMapRateLimiter");
//    }
}