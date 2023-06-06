package com.github.danrog303.safetyapi.api.photo;

import com.github.danrog303.safetyapi.data.log.EventLogEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
public class PhotoAnalysisController {
    private final PhotoAnalysisService photoAnalysisService;

    @PostMapping
    public EventLogEntry postImage(@RequestParam MultipartFile imageToAnalyse) {
        return photoAnalysisService.analyseImage(imageToAnalyse);
    }
}
