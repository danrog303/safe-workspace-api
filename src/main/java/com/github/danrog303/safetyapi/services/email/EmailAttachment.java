package com.github.danrog303.safetyapi.services.email;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class EmailAttachment {
    private String attachmentName;
    private InputStream attachmentInputStream;
}
