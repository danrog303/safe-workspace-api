package com.github.danrog303.safetyapi.services.email;

import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Service that sends emails using {@link JavaMailSender}.
 */
@Service
@RequiredArgsConstructor
public class SpringEmailSender implements EmailSender {
    private final static Logger LOGGER = LoggerFactory.getLogger(SpringEmailSender.class);
    private final JavaMailSender mailSender;
    private final SpringEmailConfig springEmailConfig;

    @Async
    @Override
    public void send(String to, String subject, String messageHtml) {
        try {
            var mimeMessage = mailSender.createMimeMessage();
            var mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            mimeMessageHelper.setText(messageHtml, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(springEmailConfig.getSenderAddress());
            mailSender.send(mimeMessage);
        } catch(MessagingException e) {
            LOGGER.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email", e);
        }
    }

    @Override
    public void send(String to, String subject, String messageHtml, List<EmailAttachment> attachments) {
        try {
            var mimeMessage = mailSender.createMimeMessage();
            var mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setText(messageHtml, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);

            for (EmailAttachment attachment : attachments) {
                byte[] fileBytes = attachment.getAttachmentInputStream().readAllBytes();
                mimeMessageHelper.addAttachment(attachment.getAttachmentName(), new ByteArrayResource(fileBytes));
            }

            mimeMessageHelper.setFrom(springEmailConfig.getSenderAddress());
            mailSender.send(mimeMessage);
        } catch(MessagingException | IOException e) {
            LOGGER.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email", e);
        }
    }
}