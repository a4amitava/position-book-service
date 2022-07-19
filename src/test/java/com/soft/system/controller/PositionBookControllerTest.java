package com.soft.system.controller;

import com.soft.system.exception.ApplicationException;
import com.soft.system.model.Event;
import com.soft.system.model.PositionBook;
import com.soft.system.processor.EventProcessor;
import com.soft.system.service.PositionBookService;
import com.soft.system.utils.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PositionBookControllerTest {
    @LocalServerPort
    private int port;
    @MockBean
    private EventProcessor eventProcessor;
    @MockBean
    private PositionBookService positionBookService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final String baseUrl = "http://localhost:%s/api/positions";

    private final List<Event> sampleTradeEvents = List.of(TestData.BUY_EVENT_FOR_ACC1_SEC1, TestData.ANOTHER_BUY_EVENT_FOR_ACC1_SEC1, TestData.SELL_EVENT_FOR_ACC1_SEC1);

    @Test
    void verifyWhenNoEventIsProcessedInSystemThenEmptySetPositionBookIsReturned() {
        // Day 1 use case when there is no event has triggered and user request for position book
        when(positionBookService.getPositions()).thenReturn(emptySet());
        ResponseEntity<List<PositionBook>> responseEntity =
                testRestTemplate.exchange(String.format(baseUrl, port),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(requireNonNull(responseEntity.getBody()).isEmpty());
    }

    @Test
    @DisplayName("when all positions are fetched")
    void verifyUserSuccessfullyRetrieveAllPositionBooks() {
        when(positionBookService.getPositions()).thenReturn(Set.of(TestData.BUY_POSITION_ACC1_SEC1, TestData.BUY_SELL_CANCELLED_POSITION_ACC1_SEC1));
        ResponseEntity<List<PositionBook>> responseEntity =
                testRestTemplate.exchange(String.format(baseUrl, port),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Set.of(TestData.BUY_POSITION_ACC1_SEC1, TestData.BUY_SELL_CANCELLED_POSITION_ACC1_SEC1), new HashSet<>(requireNonNull(responseEntity.getBody())));
    }

    @Test
    void testWhenExceptionOccurredWhileRetrievingPosition() {
        when(positionBookService.getPositions()).thenThrow(new ApplicationException("Operation Timed out", new Throwable()));
        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(String.format(baseUrl, port),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("when User fetch positions by Account and Security Information")
    void verifyUserSuccessfullyRetrievePositionFilteredByAccountAndSecurity() {
        final String account = "ACC1";
        final String security = "SEC1";
        when(positionBookService.getPositions(account, security)).thenReturn(Set.of(TestData.BUY_POSITION_ACC1_SEC1));
        ResponseEntity<List<PositionBook>> responseEntity =
                testRestTemplate.exchange(String.format(baseUrl + "/" + account + "/" + security, port),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Set.of(TestData.BUY_POSITION_ACC1_SEC1), new HashSet<>(requireNonNull(responseEntity.getBody())));
    }

    @Test
    @DisplayName("when no record found for the provided by Account and Security Information")
    void verifyNoRecordReturnedForTheProvidedAccountAndSecurity() {
        final String account = "ACC100";
        final String security = "SEC1";
        when(positionBookService.getPositions(account, security)).thenReturn(Set.of());
        ResponseEntity<List<PositionBook>> responseEntity =
                testRestTemplate.exchange(String.format(baseUrl + "/" + account + "/" + security, port),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(requireNonNull(responseEntity.getBody()).isEmpty());
    }

    @Test
    void testWhenTradeEventTriggeredSuccessfully() {
        doNothing().when(eventProcessor).accept(sampleTradeEvents);
        ResponseEntity<String> response = testRestTemplate.postForEntity(String.format(baseUrl + "/events", port), sampleTradeEvents, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(eventProcessor, times(1)).accept(sampleTradeEvents);
    }

}