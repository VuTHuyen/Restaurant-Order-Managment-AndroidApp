package com.example.orderrestaurantapp.menu;

import java.util.Map;

public class Value {
    private String id;
    private Map<String, Object> map;

    /**
     * this is a constructor of a Value object.
     *
     * @param id String
     * @param map Map<String,Object>
     */
    public Value(String id, Map<String, Object> map) {
        this.id = id;
        this.map = map;
    }
    public String getId(){
        return this.id;
    }
    public Map<String, Object> getMap(){
        return this.map;
    }
}