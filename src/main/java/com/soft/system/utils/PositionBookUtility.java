package com.soft.system.utils;

import com.soft.system.model.Action;
import com.soft.system.model.Event;
import com.soft.system.model.PositionBook;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class PositionBookUtility {
    public static PositionBook positionBookBuilder(String account, String security, List<Event> events) {
        Integer eligibleQuantity = calculateEligibleQuantityExcludingCancelledEvent(events);
        log.info("Total quantity {} for account{} , security{}", eligibleQuantity, account, security);
        return PositionBook.builder()
                .account(account)
                .security(security)
                .quantity(eligibleQuantity)
                .events(events)
                .build();
    }

    private Integer calculateEligibleQuantityExcludingCancelledEvent(List<Event> events) {
        Set<Integer> cancelledEventIds = getCancelledEventIds.apply(events);
        return events.stream()
                .filter(event -> !cancelledEventIds.contains(event.getId()))
                .map(appliedQuantity)
                .reduce(0, Integer::sum);
    }

    private static final Function<List<Event>, Set<Integer>> getCancelledEventIds = events -> events.stream()
            .filter(event -> Action.CANCEL.equals(event.getAction()))
            .map(Event::getId)
            .collect(Collectors.toSet());

    private static final Function<Event, Integer> appliedQuantity = event -> {
        if (event.getAction().equals(Action.SELL)) {
            return -event.getQuantity();
        }
        return event.getQuantity();
    };
}
