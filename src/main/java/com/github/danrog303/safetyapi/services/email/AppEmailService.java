package com.github.danrog303.safetyapi.services.email;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.InputStream;
import java.util.List;

/**
 * Service responsible for instantiating email templates
 * and sending them to the specified email address.
 */
@Service @RequiredArgsConstructor
public class AppEmailService {
    private final EmailSender emailSender;
    private final TemplateEngine emailTemplateEngine;

    @Async
    public void sendDangerInformationEmail(String toEmailAddress, String cameraName, InputStream image) {
        Context emailCtx = new Context();
        emailCtx.setVariable("cameraName", cameraName);

        String emailBody = emailTemplateEngine.process("email/danger-info", emailCtx);
        String emailSubject = "SafetyAPI: a danger on camera was detected";
        EmailAttachment attachment = new EmailAttachment("danger-img.bin", image);

        emailSender.send(toEmailAddress, emailSubject, emailBody, List.of(attachment));
    }

}