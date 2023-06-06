package com.github.danrog303.safetyapi.api.photo;

import com.github.danrog303.safetyapi.data.client.UserInfo;
import com.github.danrog303.safetyapi.data.client.UserInfoRepository;
import com.github.danrog303.safetyapi.data.equipment.DetectedPerson;
import com.github.danrog303.safetyapi.data.equipment.SafetyEquipment;
import com.github.danrog303.safetyapi.data.log.EventLogEntry;
import com.github.danrog303.safetyapi.data.log.EventLogEntryRepository;
import com.github.danrog303.safetyapi.services.detection.ImageDetectionService;
import com.github.danrog303.safetyapi.services.email.AppEmailService;
import com.github.danrog303.safetyapi.services.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoAnalysisService {
    private final ImageDetectionService imageDetectionService;
    private final AuthenticatedUserService authenticatedUserService;
    private final UserInfoRepository userInfoRepository;
    private final EventLogEntryRepository eventLogRepository;
    private final AppEmailService emailService;

    @Value("${safetyapi.image-submit.threshold}")
    private long imageSubmitThreshold;

    /**
     * Performs the AI analysis on the given image file and returns the analysis result.
     * The result of the analysis contains information about people on the photo and their safety equipment.
     */
    @PreAuthorize("hasAuthority('UPLOAD_PHOTOS')")
    public EventLogEntry analyseImage(MultipartFile imageToAnalyse) {
        List<DetectedPerson> detectedPeople;
        UserInfo currentUser = authenticatedUserService.getAuthenticatedUserInfo();

        if (new Date().getTime() - currentUser.getLastAction().getTime() < imageSubmitThreshold) {
            String exceptionMsg = String.format("Threshold for image analysis is %s ms.", imageSubmitThreshold);
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, exceptionMsg);
        }

        try {
            detectedPeople = imageDetectionService.getDetectedPeople(imageToAnalyse.getInputStream(),
                    imageToAnalyse.getSize());
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        EventLogEntry analysisResult = new EventLogEntry(
                UUID.randomUUID().toString(),
                new Date(),
                currentUser.getLogin(),
                isDangerDetected(detectedPeople),
                detectedPeople
        );

        try {
            if (analysisResult.isDangerDetected()) {
                handleDanger(currentUser, imageToAnalyse.getInputStream());
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }


        currentUser.setLastAction(new Date());
        userInfoRepository.save(currentUser);
        eventLogRepository.save(analysisResult);
        return analysisResult;
    }

    private void handleDanger(UserInfo dangerSubject, InputStream dangerImage) {
        for (String managerEmail : dangerSubject.getReportTo()) {
            emailService.sendDangerInformationEmail(managerEmail, dangerSubject.getDisplayName(), dangerImage);
        }
    }


    /**
     * Checks if image contains a dangerous situation (if there is a person without the required safety equipment).
     * Danger is not raised, when the situation is unambiguous (for example safety gloves are needed, but hands
     * of the person on the photo are not visible).
     */
    private boolean isDangerDetected(List<DetectedPerson> detectedPeople) {
        if (detectedPeople.isEmpty()) {
            return false;
        }

        UserInfo currentUser = authenticatedUserService.getAuthenticatedUserInfo();
        for (DetectedPerson person : detectedPeople) {
            if (currentUser.getRequiredEquipment().contains(SafetyEquipment.FACE_COVER) && Boolean.FALSE.equals(person.getHasFaceCover())) {
                return true;
            }
            if (currentUser.getRequiredEquipment().contains(SafetyEquipment.HAND_COVER) && Boolean.FALSE.equals(person.getHasHandsCover())) {
                return true;
            }
            if (currentUser.getRequiredEquipment().contains(SafetyEquipment.HEAD_COVER) && Boolean.FALSE.equals(person.getHasHeadCover())) {
                return true;
            }
        }

        return false;
    }
}
