package com.github.danrog303.safetyapi.services.database;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.github.danrog303.safetyapi.data.client.UserInfoRepository;
import com.github.danrog303.safetyapi.data.log.EventLogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;

/**
 * Exposes Spring Beans related to Amazon DynamoDB database.
 */
@Configuration
@EnableDynamoDBRepositories(basePackageClasses={UserInfoRepository.class, EventLogEntryRepository.class})
@RequiredArgsConstructor
public class DynamoDbConfig {
    private final AwsCredentials awsCredentials;
    private final Region awsRegion;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        // Mapping AWS SDK v2 beans to SDK v1
        // (Derjust's DynamoDB <-> Spring Data library operates on AWS SDK v1)
        AWSCredentials sdkV1Credentials = new BasicAWSCredentials(awsCredentials.accessKeyId(), awsCredentials.secretAccessKey());
        AWSCredentialsProvider sdkV1CredentialsProvider = new AWSStaticCredentialsProvider(sdkV1Credentials);
        Regions sdkV1Region = Regions.fromName(awsRegion.toString());

        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(sdkV1CredentialsProvider)
                .withRegion(sdkV1Region)
                .build();
    }
}
