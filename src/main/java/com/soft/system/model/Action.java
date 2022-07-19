package com.soft.system.model;

public enum Action {
    BUY(""),
    SELL("Sell"),
    CANCEL("Cancel");

    private final String name;

    Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
