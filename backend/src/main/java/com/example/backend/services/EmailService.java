package com.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${spring.mail.host:}")
    private String mailHost;

    public void sendPasswordResetCode(String toEmail, String code) {
        if (mailHost == null || mailHost.isBlank()) {
            log.warn("SMTP no configurado. Codigo de recuperacion para {}: {}", toEmail, code);
            return;
        }

        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) {
            log.warn("JavaMailSender no disponible. Codigo de recuperacion para {}: {}", toEmail, code);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        if (fromEmail != null && !fromEmail.isBlank()) {
            message.setFrom(fromEmail);
        }
        message.setTo(toEmail);
        message.setSubject("Codigo para recuperar tu cuenta");
        message.setText("""
                Hola,

                Tu codigo para cambiar la contrasena es: %s

                Este codigo vence en 10 minutos. Si no solicitaste este cambio, ignora este correo.
                """.formatted(code));

        mailSender.send(message);
    }
}
