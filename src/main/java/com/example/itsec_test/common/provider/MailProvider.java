package com.example.itsec_test.common.provider;

public interface MailProvider {
    void sendMail(String to, String subject, String body);
}
