package com.jpmc.ib.model;

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
