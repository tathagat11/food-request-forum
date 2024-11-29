package com.homey.foodforum.events;

public interface Event {
    String getEventId();
    String getType();
    Long getTimestamp();
}
