package com.soft.system.utils;

import com.soft.system.model.Event;
import com.soft.system.model.PositionBook;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.soft.system.utils.PositionBookUtility.positionBookBuilder;
import static com.soft.system.utils.TestData.ANOTHER_BUY_EVENT_FOR_ACC1_SEC1;
import static com.soft.system.utils.TestData.BUY_EVENT_FOR_ACC1_SEC1;
import static com.soft.system.utils.TestData.CANCELLED_EVENT_ACC1_SEC1;
import static com.soft.system.utils.TestData.SELL_EVENT_FOR_ACC1_SEC1;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionBookUtilityTest {

    @Test
    void testPositionBookForOnlyBuyEvent() {
        List<Event> buyEvents = List.of(BUY_EVENT_FOR_ACC1_SEC1, ANOTHER_BUY_EVENT_FOR_ACC1_SEC1);
        PositionBook positionBook = positionBookBuilder("ACC1", "SEC1", buyEvents);
        assertEquals(150, positionBook.getQuantity());
        assertEquals(buyEvents, positionBook.getEvents());
        assertEquals("ACC1", positionBook.getAccount());
        assertEquals("SEC1", positionBook.getSecurity());
    }

    @Test
    void testPositionBookForBuyAndSellEvent() {
        List<Event> buyAndSellEvents = List.of(BUY_EVENT_FOR_ACC1_SEC1, ANOTHER_BUY_EVENT_FOR_ACC1_SEC1, SELL_EVENT_FOR_ACC1_SEC1);
        PositionBook positionBook = positionBookBuilder("ACC1", "SEC1", buyAndSellEvents);
        assertEquals(100, positionBook.getQuantity());
        assertEquals(buyAndSellEvents, positionBook.getEvents());
        assertEquals("ACC1", positionBook.getAccount());
        assertEquals("SEC1", positionBook.getSecurity());
    }

    @Test
    void testPositionBookForBuySellAndCancelledEvent() {
        List<Event> buyAndSellEvents = List.of(BUY_EVENT_FOR_ACC1_SEC1, ANOTHER_BUY_EVENT_FOR_ACC1_SEC1, SELL_EVENT_FOR_ACC1_SEC1, CANCELLED_EVENT_ACC1_SEC1);
        PositionBook positionBook = positionBookBuilder("ACC1", "SEC1", buyAndSellEvents);
        assertEquals(150, positionBook.getQuantity());
        assertEquals(buyAndSellEvents, positionBook.getEvents());
        assertEquals("ACC1", positionBook.getAccount());
        assertEquals("SEC1", positionBook.getSecurity());
    }

}