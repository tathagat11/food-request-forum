package com.homey.foodforum.models;

import java.math.BigDecimal;
import java.time.Duration;

import com.homey.foodforum.models.enums.ResponseStatus;

import lombok.Data;

@Data
public class ChefResponse {
    private String id;
    private String chefId;
    private String requestId;
    private BigDecimal price;
    private Duration preparationTime;
    private ResponseStatus status;
}