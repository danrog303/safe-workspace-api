package com.github.danrog303.safetyapi.services.detection;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;

@Configuration
@RequiredArgsConstructor
public class AwsRekognitionConfig {
    private final AwsCredentialsProvider awsCredentials;
    private final Region awsRegion;

    @Bean
    public RekognitionClient rekognitionClient() {
        return RekognitionClient.builder()
                .credentialsProvider(awsCredentials)
                .region(awsRegion)
                .build();
    }
}
