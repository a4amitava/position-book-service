package com.jpmc.ib.controller;

import com.jpmc.ib.exception.ApplicationException;
import com.jpmc.ib.model.Event;
import com.jpmc.ib.model.PositionBook;
import com.jpmc.ib.processor.EventProcessor;
import com.jpmc.ib.service.PositionBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
