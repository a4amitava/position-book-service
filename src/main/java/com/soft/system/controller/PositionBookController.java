package com.soft.system.controller;

import com.soft.system.exception.ApplicationException;
import com.soft.system.model.Event;
import com.soft.system.model.PositionBook;
import com.soft.system.processor.EventProcessor;
import com.soft.system.service.PositionBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/positions")
public class PositionBookController {
    private final EventProcessor eventProcessingService;
    private final PositionBookService positionBookService;

    @GetMapping
    public CompletableFuture<Set<PositionBook>> allPositions() {
        log.info("Attempting to retrieve all positions");
        return CompletableFuture.supplyAsync(positionBookService::getPositions);
    }

    @GetMapping("{account}/{security}")
    public CompletableFuture<Set<PositionBook>> getPositionByAccountAndSecurity(@PathVariable final String account,
                                                                                @PathVariable final String security) {
        log.info("Attempting to retrieve position by account {}, security{}", account, security);
        return CompletableFuture.supplyAsync(() -> positionBookService.getPositions(account, security));
    }

    @PostMapping("/events")
    public ResponseEntity<String> process(@RequestBody List<Event> events) {
        CompletableFuture.runAsync(() -> eventProcessingService.accept(events));
        return ResponseEntity.ok("Events submitted for Processing");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ApplicationException.class)
    public Set<String> handleApplicationInternalExceptions(
            ApplicationException ex) {
        return Set.of(ex.getErrorDescription());
    }
}
