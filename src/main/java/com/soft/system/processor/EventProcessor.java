package com.soft.system.processor;

import com.soft.system.exception.ApplicationException;
import com.soft.system.model.Event;
import com.soft.system.model.PositionBook;
import com.soft.system.repo.PositionBookRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.soft.system.utils.PositionBookUtility.positionBookBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventProcessor implements Consumer<List<Event>> {

    private final PositionBookRepo positionBookRepo;

    @Override
    public void accept(List<Event> events) {
        try {
            Set<PositionBook> positionBooks = toPositionBooks.apply(events);
            log.info("event processed.");
            positionBookRepo.save(positionBooks);
        } catch (Exception exception) {
            log.error("Unable to process the incoming events {}", events, exception);
            throw new ApplicationException("Error in processing trade events, please check the payload and/or server logs for more details", exception);
        }
    }

    private final Function<List<Event>, Set<PositionBook>> toPositionBooks = (events) -> {
        Map<Pair<String, String>, List<Event>> eventsGroupedByAccountAndSecurity = events.stream()
                .collect(Collectors.groupingBy(e -> Pair.of(e.getAccount(), e.getSecurity())));
        return eventsGroupedByAccountAndSecurity.entrySet().stream()
                .map(entry -> positionBookBuilder(entry.getKey().getLeft(), entry.getKey().getRight(), entry.getValue()))
                .collect(Collectors.toUnmodifiableSet());
    };
}
