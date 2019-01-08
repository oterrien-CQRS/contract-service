package com.ote.mandate.business;

import java.util.*;

public class ScenarioContext {

    private final static Map<String, Object> input = new HashMap<>();
    private final static Map<String, Object> output = new HashMap<>();

    public enum Type {
        INPUT, OUTPUT
    }

    public void put(String key, Object value, Type type) {
        getMap(type).put(key, value);
    }

    public void put(String key, List value, Type type) {
        ((List) getMap(type).computeIfAbsent(key, p -> new ArrayList<>())).add(value);
    }

    public <T> Optional<T> get(String key, Type type) {
        return Optional.ofNullable((T) getMap(type).get(key));
    }

    public void clean() {
        input.clear();
        output.clear();
    }

    private Map<String, Object> getMap(Type type) {
        switch (type) {
            case INPUT:
                return input;
            case OUTPUT:
            default:
                return output;
        }
    }
}
