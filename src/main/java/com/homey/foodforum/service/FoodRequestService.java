package com.homey.foodforum.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.homey.foodforum.consumers.FoodRequestConsumer;
import com.homey.foodforum.consumers.NotificationConsumer;
import com.homey.foodforum.consumers.TagMatchingConsumer;
import com.homey.foodforum.events.ChefNotificationEvent;
import com.homey.foodforum.events.FoodRequestEvent;
import com.homey.foodforum.models.FoodRequest;
import com.homey.foodforum.models.enums.RequestStatus;
import com.homey.foodforum.producers.FoodRequestProducer;

// service/FoodRequestService.java
public class FoodRequestService {
    private final FoodRequestProducer requestProducer;
    private final FoodRequestConsumer requestConsumer;
    private final TagMatchingConsumer tagConsumer;
    private final NotificationConsumer notificationConsumer;
    private Map<String, FoodRequest> requests = new ConcurrentHashMap<>();

    public FoodRequestService() {
        this.requestProducer = new FoodRequestProducer();
        this.requestConsumer = new FoodRequestConsumer();
        this.tagConsumer = new TagMatchingConsumer();
        this.notificationConsumer = new NotificationConsumer();

        new Thread(requestConsumer::consume).start();
        new Thread(tagConsumer::consume).start();
        new Thread(notificationConsumer::consume).start();
    }
 
    public void createFoodRequest(FoodRequest request) {
        request.setStatus(RequestStatus.NEW);
        requests.put(request.getId(), request);
        
        FoodRequestEvent event = new FoodRequestEvent(
            UUID.randomUUID().toString(), 
            "FOOD_REQUEST",
            System.currentTimeMillis(),
            request.getId(),
            request.getDishName(),
            request.getLocation().getCity(),
            request.getDescription(),
            request.getStatus().toString(),
            request.getLocation().getArea(),
            request.getTags()
        );
        
        requestProducer.sendFoodRequest(event);
    }

    public List<ChefNotificationEvent> getChefNotifications(String chefId) {
        return notificationConsumer.getChefNotifications(chefId);
    }
 
    public FoodRequest getFoodRequest(String id) {
        return requests.get(id);
    }
 
    public List<FoodRequest> getActiveRequests() {
        return requests.values().stream()
            .filter(r -> r.getStatus() != RequestStatus.COMPLETED)
            .collect(Collectors.toList());
    }
 }