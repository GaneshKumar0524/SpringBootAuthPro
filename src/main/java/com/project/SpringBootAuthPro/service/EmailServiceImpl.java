package com.project.SpringBootAuthPro.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(String to, String subject, String body) {
        // For now, just log it
        log.info("EMAIL SENT TO: {}\nSUBJECT: {}\nBODY: {}", to, subject, body);
    }
}





