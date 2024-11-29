package com.homey.foodforum.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    private String city;
    private String area;
    private String address;
}
