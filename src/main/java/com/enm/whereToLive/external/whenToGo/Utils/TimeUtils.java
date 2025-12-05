package com.enm.whereToLive.external.whenToGo.Utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter OUT_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /** now 기준, 과거(자기 자신 포함) 가장 가까운 오전 8시를 문자열로 반환 */
    public static String last8AMString() {
        return last8AM(ZonedDateTime.now(KST)).format(OUT_FMT);
    }

    /** 테스트 용이하게 임의 기준시각을 받아 처리 (타임존 포함) */
    public static ZonedDateTime last8AM(ZonedDateTime reference) {
        ZonedDateTime eightToday = reference.withHour(8).withMinute(0).withSecond(0).withNano(0);
        // 기준시각이 8시 이전이면 전날 8시, 8시 이후/같으면 오늘 8시
        if (reference.isBefore(eightToday)) {
            return eightToday.minusDays(1);
        }
        return eightToday;
    }
}