package com.ote.mandate.business;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ScenarioContext {

    private final static Map<String, String> input = new HashMap<>();
    private final static Map<String, String> output = new HashMap<>();

    public enum Type {
        INPUT, OUTPUT
    }

    public void put(String key, String value, Type type) {
        getMap(type).put(key, value);
    }

    public Optional<String> get(String key, Type type) {
        return Optional.ofNullable(getMap(type).get(key));
    }

    public void clean() {
        input.clear();
        output.clear();
    }

    private Map<String, String> getMap(Type type) {
        switch (type) {
            case INPUT:
                return input;
            case OUTPUT:
            default:
                return output;
        }
    }
}
