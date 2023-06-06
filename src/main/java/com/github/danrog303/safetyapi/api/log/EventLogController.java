package com.github.danrog303.safetyapi.api.log;

import com.github.danrog303.safetyapi.data.log.EventLogEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class EventLogController {
    private final EventLogService eventLogService;

    @GetMapping
    public List<EventLogEntry> getLogs(@RequestParam Date from, @RequestParam Date to) {
        return eventLogService.getEventLog(from, to);
    }
}
