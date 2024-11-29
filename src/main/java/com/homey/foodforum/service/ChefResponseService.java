package com.homey.foodforum.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.homey.foodforum.events.ResponseEvent;
import com.homey.foodforum.models.ChefResponse;
import com.homey.foodforum.producers.ResponseProducer;

public class ChefResponseService {
    private final ResponseProducer responseProducer;
    private Map<String, List<ChefResponse>> requestResponses = new ConcurrentHashMap<>();
 
    public ChefResponseService() {
        this.responseProducer = new ResponseProducer();
    }
 
    public void createResponse(ChefResponse response) {
        requestResponses.computeIfAbsent(response.getRequestId(), k -> new ArrayList<>())
            .add(response);
 
        ResponseEvent event = new ResponseEvent(
            UUID.randomUUID().toString(),
            "CHEF_RESPONSE",
            System.currentTimeMillis(),
            response.getId(),
            response.getChefId(),
            response.getRequestId(),
            response.getPrice().doubleValue(),
            response.getPreparationTime().toString(),
            response.getStatus().toString()
        );
        
        responseProducer.sendResponse(event);
    }
 
    public List<ChefResponse> getResponsesForRequest(String requestId) {
        return requestResponses.getOrDefault(requestId, new ArrayList<>());
    }
 }