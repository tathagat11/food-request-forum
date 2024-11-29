package com.homey.foodforum.events;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequestEvent implements Event {
    private String eventId;
    private String type;
    private Long timestamp;
    private String requestId;
    private String dishName;
    private String regionalOrigin;
    private String description;
    private String requestStatus;
    private String location;
    private List<String> tags;
}
