package com.homey.foodforum.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEvent implements Event {
    private String eventId;
    private String type;
    private Long timestamp;
    private String responseId;
    private String chefId;
    private String requestId;
    private Double proposedPrice;
    private String preparationTimeline;
    private String availability;
}
