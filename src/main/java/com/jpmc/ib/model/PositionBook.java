package com.jpmc.ib.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
@Builder
public class PositionBook {
    private final String account;
    private final String security;
    private final Integer quantity;
    @Builder.Default
    private final List<Event> events = new ArrayList<>();
}
