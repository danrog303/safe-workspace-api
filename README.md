# safe-workspace-api
![activity badge](https://img.shields.io/github/last-commit/danrog303/safe-workspace-api)
![license badge](https://shields.io/github/license/danrog303/safe-workspace-api)

> API for ensuring a safe workspace environment using the image recognition AI.

## 👨‍💼 What does it exactly do?
The application allows to ensure a safe working environment in places with a higher level of danger (for example, construction sites or factories).

There should be cameras set up in the workplace, and those cameras should be connected to a computer (one or multiple). 
The computers take a photo every certain period of time and send it to the `POST /api/v1/photo` HTTP endpoint. 
Then, the server evaluates whether the people in the photo have personal protective equipment on
and the result of this evaluation is stored in a database. Currently, the program is able to recognize face, head and hand protection.

At any time, the site manager can retrieve the historical data using the `GET /api/v1/logs?from=yyyy-MM-dd&to=yyyy-MM-dd` endpoint.

Additionally, the configured email addresses get a notification email when a danger was detected on some of the cameras.

## 🎓 Note
The application was created as a project assignment during the fourth semester of studies at [Bydgoszcz University of Science and Technology](https://pbs.edu.pl/).

## ⚙️ Tech stack
- Language: Java 11
- Framework: Spring Boot 2.7.9
- Runtime: AWS Lambda (or a regular Java runtime)
- Image detection service: AWS Rekognition
- Email service: Spring Mail + AWS SES
- Database: DynamoDB
- Authentication: Spring Security

## 🔨 How to run a instance of the API?
### Local instance 
Project uses Maven, so it can be run like a regular Maven project. Before running the app, make sure that you 
have all required environmental variables set. For the list of required environmental variables, refer to `application.properties` file.
```
./mvnw spring-boot:run
```

### AWS Lambda instance
Application can be deployed to the AWS cloud using a `serverless` toolkit.
```
./mvnw package -P aws
serverless deploy
```

## ⚙️ Client apps
This repository contains only the API for a **safe-workspace** project. 
For camera client and dashboard client, you can visit those two repositories: [(link 1)](https://github.com/Alexis2502/ClientCameraDetection), [(link 2)](https://github.com/Alexis2502/DashboardCameraDetectionEventLog)