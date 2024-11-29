package com.homey.foodforum.models;

import java.util.List;

import com.homey.foodforum.models.enums.RequestStatus;

import lombok.Data;

@Data
public class FoodRequest {
    private String id;
    private String customerId;
    private String dishName;
    private String description;
    private RequestStatus status;
    private Location location;
    private List<ChefResponse> responses;
    private List<String> tags;
}
