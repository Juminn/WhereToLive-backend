package com.enm.whereToLive.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Destination {
    String name;
    Double lat;
    Double lng;
}
