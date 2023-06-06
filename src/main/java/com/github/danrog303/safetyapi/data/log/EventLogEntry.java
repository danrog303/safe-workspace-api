package com.github.danrog303.safetyapi.data.log;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.github.danrog303.safetyapi.data.equipment.DetectedPerson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data @AllArgsConstructor @NoArgsConstructor
@DynamoDBTable(tableName="safety-api-event-log")
public class EventLogEntry {
    @DynamoDBHashKey
    private String eventId;

    @DynamoDBAttribute
    private Date date;

    @DynamoDBAttribute
    private String userLogin;

    @DynamoDBAttribute
    private boolean dangerDetected;

    @DynamoDBAttribute
    private List<DetectedPerson> detectedPeople;
}
