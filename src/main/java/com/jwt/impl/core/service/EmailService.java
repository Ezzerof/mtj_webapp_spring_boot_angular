package com.jwt.impl.core.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendEmail(String subject, String message, String to) throws MessagingException;
}
