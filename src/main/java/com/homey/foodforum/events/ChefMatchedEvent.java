package com.homey.foodforum.events;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChefMatchedEvent implements Event {
    private String eventId;
    private String type;
    private Long timestamp;
    private String chefId;
    private String requestId;
    private List<String> matchedTags;
}
