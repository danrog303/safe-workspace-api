package com.github.danrog303.safetyapi.data.equipment;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single person that was detected on the photo.
 */
@DynamoDBDocument
@Data @AllArgsConstructor @NoArgsConstructor
public class DetectedPerson {
    @DynamoDBAttribute
    private Boolean hasFaceCover;

    @DynamoDBAttribute
    private Boolean hasHandsCover;

    @DynamoDBAttribute
    private Boolean hasHeadCover;
}
