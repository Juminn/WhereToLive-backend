package com.enm.whereToLive.data.dabang;

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

