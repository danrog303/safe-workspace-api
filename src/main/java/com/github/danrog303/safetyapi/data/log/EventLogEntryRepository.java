package com.github.danrog303.safetyapi.data.log;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

@EnableScan
public interface EventLogEntryRepository extends CrudRepository<EventLogEntry, String> {
    List<EventLogEntry> findAllByDateBetween(Date dateFrom, Date dateTo);
}
