package com.github.danrog303.safetyapi.services.detection;

import com.github.danrog303.safetyapi.data.equipment.DetectedPerson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AwsRekognitionImageDetectionService implements ImageDetectionService {
    private final RekognitionClient rekognitionClient;

    @Override
    public List<DetectedPerson> getDetectedPeople(InputStream imageStream, long imageSize) {
        SdkBytes sdkBytes = SdkBytes.fromInputStream(imageStream);

        DetectProtectiveEquipmentRequest request = DetectProtectiveEquipmentRequest
                .builder()
                .image(Image.builder().bytes(sdkBytes).build())
                .build();

        DetectProtectiveEquipmentResponse response = rekognitionClient.detectProtectiveEquipment(request);

        List<DetectedPerson> detectedPeople = new ArrayList<>();

        for (ProtectiveEquipmentPerson rekognitionDetectedPerson : response.persons()) {
            DetectedPerson detectedPerson = new DetectedPerson();
            for (ProtectiveEquipmentBodyPart rekognitionBodyPart : rekognitionDetectedPerson.bodyParts()) {
                switch(rekognitionBodyPart.name()) {
                    case FACE:
                        detectedPerson.setHasFaceCover(!rekognitionBodyPart.equipmentDetections().isEmpty());
                        break;

                    case LEFT_HAND:
                    case RIGHT_HAND:
                        detectedPerson.setHasHandsCover(!rekognitionBodyPart.equipmentDetections().isEmpty());
                        break;

                    case HEAD:
                        detectedPerson.setHasHeadCover(!rekognitionBodyPart.equipmentDetections().isEmpty());
                        break;
                }
            }
            detectedPeople.add(detectedPerson);
        }

        return detectedPeople;
    }
}
