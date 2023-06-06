package com.github.danrog303.safetyapi.services.detection;

import com.github.danrog303.safetyapi.data.equipment.DetectedPerson;

import java.io.InputStream;
import java.util.List;

public interface ImageDetectionService {
    List<DetectedPerson> getDetectedPeople(InputStream imageStream, long imageSize);
}