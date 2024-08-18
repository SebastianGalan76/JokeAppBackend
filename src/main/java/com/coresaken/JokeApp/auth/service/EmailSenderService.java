package com.coresaken.JokeApp.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    final JavaMailSender mailSender;
    @Value("${website.address}")
    String websiteAddress;

    @Value("${website.domain}")
    String websiteDomain;

    //TODO Change emails preview
    public void sendActiveAccountEmail(String to, String activeAccountToken) throws MessagingException {
        String activeAccountLink = websiteAddress + "auth/active/" + activeAccountToken;

        String activeAccountEmail = "<html>"
                + activeAccountLink
                + "</html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom("JokeApp@" + websiteDomain);
        helper.setSubject("Aktywacja konta użytkownika");
        helper.setText(activeAccountEmail, true);

        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String to, String token) throws MessagingException {
        String resetPasswordLink = websiteAddress + "auth/resetPassword?token=" + token;

        String resetPasswordEmail = "<html>"
                + resetPasswordLink
                + "</html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom("JokeApp@" + websiteDomain);
        helper.setSubject("Resetowanie hasła");
        helper.setText(resetPasswordEmail, true);

        mailSender.send(message);
    }
}
