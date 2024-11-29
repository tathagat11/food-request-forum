package com.homey.foodforum.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Chef {
    private String id;
    private String name;
    private List<String> tags;
}
