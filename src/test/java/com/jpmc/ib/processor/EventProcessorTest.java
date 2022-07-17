package com.jpmc.ib.processor;

import com.jpmc.ib.exception.ApplicationException;
import com.jpmc.ib.model.PositionBook;
import com.jpmc.ib.repo.PositionBookRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static com.jpmc.ib.utils.TestData.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventProcessorTest {
    @InjectMocks
    private EventProcessor eventProcessor;
    @Mock
    private PositionBookRepo positionBookRepo;

    @Captor
    private ArgumentCaptor<Set<PositionBook>> positionBookCaptor;

    @Test
    void testBuyingSecuritiesOfSameAccountAndSecurity() {
        eventProcessor.accept(List.of(BUY_EVENT_FOR_ACC1_SEC1, ANOTHER_BUY_EVENT_FOR_ACC1_SEC1));
        verify(positionBookRepo).save(positionBookCaptor.capture());
        Set<PositionBook> positionBooks = positionBookCaptor.getValue();
        verify(positionBookRepo, times(1)).save(anySet());
        assertEquals(1, positionBooks.size());
        assertTrue(positionBooks.stream()
                .allMatch(positionBook -> positionBook.getAccount().equals(BUY_EVENT_FOR_ACC1_SEC1.getAccount()) && positionBook.getSecurity().equals(BUY_EVENT_FOR_ACC1_SEC1.getSecurity())));
    }

    @Test
    void testWhenExceptionOccurredWhileProcessingEvents() {
        doThrow(new RuntimeException("Resource Not Available")).when(positionBookRepo).save(anySet());
        ApplicationException applicationException = assertThrows(
                ApplicationException.class,
                () -> eventProcessor.accept(List.of(BUY_EVENT_FOR_ACC1_SEC1, ANOTHER_BUY_EVENT_FOR_ACC1_SEC1))
        );
        assertTrue(applicationException.getMessage().contains("Error in processing trade events, please check the payload and/or server logs for more details"));
    }

    @Test
    void testBuyingSecuritiesOfDifferentAccount() {
        eventProcessor.accept(List.of(BUY_EVENT_FOR_ACC1_SEC1, BUY_EVENT_FOR_ACC2_SECXYZ, ANOTHER_BUY_EVENT_FOR_ACC1_SEC1));
        verify(positionBookRepo).save(positionBookCaptor.capture());
        Set<PositionBook> positionBooks = positionBookCaptor.getValue();
        verify(positionBookRepo, times(1)).save(anySet());
        assertEquals(2, positionBooks.size());
        // check the buy position for ACC1 + SEC1
        List<PositionBook> positionForAcc1Sec1 = positionBooks.stream()
                .filter(positionBook -> positionBook.getAccount().equals(BUY_EVENT_FOR_ACC1_SEC1.getAccount()) && positionBook.getSecurity().equals(BUY_EVENT_FOR_ACC1_SEC1.getSecurity()))
                .collect(toList());
        assertEquals(1, positionForAcc1Sec1.size());
        // verify processed events
        assertEquals(Set.of(BUY_EVENT_FOR_ACC1_SEC1, ANOTHER_BUY_EVENT_FOR_ACC1_SEC1), positionForAcc1Sec1.stream().flatMap(positionBook -> positionBook.getEvents().stream()).collect(toSet()));
        // check the buy position for ACC2 + SECXYZ
        List<PositionBook> positionForAcc2SecXYZ = positionBooks.stream()
                .filter(positionBook -> positionBook.getAccount().equals(BUY_EVENT_FOR_ACC2_SECXYZ.getAccount()) && positionBook.getSecurity().equals(BUY_EVENT_FOR_ACC2_SECXYZ.getSecurity()))
                .collect(toList());
        assertEquals(1, positionForAcc2SecXYZ.size());
        assertEquals(Set.of(BUY_EVENT_FOR_ACC2_SECXYZ), positionForAcc2SecXYZ.stream().flatMap(positionBook -> positionBook.getEvents().stream()).collect(toSet()));
    }

    @Test
    void testBuyingAndSellingSecuritiesForSameAccount() {
        eventProcessor.accept(List.of(BUY_EVENT_FOR_ACC1_SEC1, SELL_EVENT_FOR_ACC1_SEC1));
        verify(positionBookRepo).save(positionBookCaptor.capture());
        Set<PositionBook> positionBooks = positionBookCaptor.getValue();
        verify(positionBookRepo, times(1)).save(anySet());
        assertEquals(1, positionBooks.size());
        assertEquals(Set.of(BUY_EVENT_FOR_ACC1_SEC1, SELL_EVENT_FOR_ACC1_SEC1), positionBooks.stream().flatMap(positionBook -> positionBook.getEvents().stream()).collect(toSet()));
    }

    @Test
    void testCancelledEventSecurities() {
        eventProcessor.accept(List.of(BUY_EVENT_FOR_ACC1_SEC1, SELL_EVENT_FOR_ACC1_SEC1, CANCELLED_EVENT_ACC1_SEC1));
        verify(positionBookRepo).save(positionBookCaptor.capture());
        Set<PositionBook> positionBooks = positionBookCaptor.getValue();
        verify(positionBookRepo, times(1)).save(anySet());
        assertEquals(Set.of(BUY_EVENT_FOR_ACC1_SEC1, SELL_EVENT_FOR_ACC1_SEC1, CANCELLED_EVENT_ACC1_SEC1), positionBooks.stream().flatMap(positionBook -> positionBook.getEvents().stream()).collect(toSet()));
    }
}