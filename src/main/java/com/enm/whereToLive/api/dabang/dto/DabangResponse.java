package com.enm.whereToLive.api.dabang.dto;

import com.enm.whereToLive.api.dabang.model.SubwayStation;
import lombok.Data;

import java.util.List;

@Data
public class DabangResponse {
    //public int code;
    public Result result;

    @Data
    private static class Result {
        public List<SubwayStation> subwayList;
    }

    public List<SubwayStation> getSubwayList(){
        return result.subwayList;
    }

}

