package com.homey.foodforum.service;

import com.homey.foodforum.events.FoodRequestEvent;
import com.homey.foodforum.events.ResponseEvent;

public class NotificationService {
    public void notifyChef(FoodRequestEvent event) {
        System.out.println("Notifying chefs about request: " + event.getDishName());
    }

    public void notifyCustomer(ResponseEvent event) {
        System.out.println("Notifying customer about chef response for request: " + event.getRequestId());
    }
}
