package com.jpmc.ib.utils;

import com.jpmc.ib.model.Action;
import com.jpmc.ib.model.Event;
import com.jpmc.ib.model.PositionBook;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TestData {
    public static final Event BUY_EVENT_FOR_ACC1_SEC1 = eventBuilder(1, Action.BUY, "ACC1", "SEC1", 100);
    public static final Event ANOTHER_BUY_EVENT_FOR_ACC1_SEC1 = eventBuilder(2, Action.BUY, "ACC1", "SEC1", 50);
    public static final Event SELL_EVENT_FOR_ACC1_SEC1 = eventBuilder(3, Action.SELL, "ACC1", "SEC1", 50);
    public static final Event CANCELLED_EVENT_ACC1_SEC1 = eventBuilder(3, Action.CANCEL, "ACC1", "SEC1", 50);
    public static final Event BUY_EVENT_FOR_ACC2_SECXYZ = eventBuilder(5, Action.BUY, "ACC2", "SECXYZ", 33);

    public static final PositionBook BUY_POSITION_ACC1_SEC1 = PositionBook.builder().events(List.of(BUY_EVENT_FOR_ACC1_SEC1, ANOTHER_BUY_EVENT_FOR_ACC1_SEC1)).account("ACC1").security("SEC1").quantity(100).build();
    public static final PositionBook BUY_POSITION_ACC2_SECXYZ = PositionBook.builder().events(List.of(BUY_EVENT_FOR_ACC2_SECXYZ)).account("ACC2").security("SECXYZ").quantity(100).build();
    public static final PositionBook BUY_SELL_CANCELLED_POSITION_ACC1_SEC1 = PositionBook.builder().events(List.of(SELL_EVENT_FOR_ACC1_SEC1, BUY_EVENT_FOR_ACC1_SEC1, CANCELLED_EVENT_ACC1_SEC1)).account("ACC1").security("SEC1").quantity(100).build();

    private static Event eventBuilder(Integer id,
                                      Action action,
                                      String account,
                                      String security,
                                      Integer quantity) {
        return Event.builder().id(id)
                .account(account)
                .quantity(quantity)
                .security(security)
                .action(action)
                .build();
    }
}
