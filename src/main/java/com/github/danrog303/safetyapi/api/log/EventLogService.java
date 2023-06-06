package com.github.danrog303.safetyapi.api.log;

import com.github.danrog303.safetyapi.data.log.EventLogEntry;
import com.github.danrog303.safetyapi.data.log.EventLogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventLogService {
    private final EventLogEntryRepository eventLogEntryRepository;

    @PreAuthorize("hasAuthority('GET_EVENT_LOG')")
    public List<EventLogEntry> getEventLog(Date fromDate, Date toDate) {
        // DynamoDB can't sort when scan operation is used
        // So sorting must be done by the server itself
        return eventLogEntryRepository.findAllByDateBetween(fromDate, toDate)
             .stream()
             .sorted(Comparator.comparing(EventLogEntry::getDate))
             .collect(Collectors.toList());
    }
}
