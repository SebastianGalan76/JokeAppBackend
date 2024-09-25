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
                + "Cześć! <br><br>" +
                "Dziękujemy za zarejestrowanie się w serwisie Dawka Śmiechu – miejscu, gdzie dobry humor nigdy się nie kończy! <br><br>" +
                "Aby w pełni korzystać z naszego serwisu, musisz aktywować swoje konto. Wystarczy, że klikniesz w poniższy link, aby dokończyć proces rejestracji:<br><br>" +
                "<a href=\""+activeAccountLink+"\" style=\"color: blue\">Aktywuj swoje konto </a>" +
                "<br><br>" +
                "Jeśli powyższy link nie działa, skopiuj go i wklej do przeglądarki: <br>" +
                activeAccountLink +
                "<br><br>" +
                "Jeśli to nie Ty zakładałeś konto, zignoruj tę wiadomość.<br><br>" +
                "Do zobaczenia na Dawce Śmiechu!<br>" +
                "Zespół DawkaŚmiechu"
                + "</html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom("DawkaSmiechu@" + websiteDomain);
        helper.setSubject("Dawka Śmiechu - Aktywacja konta użytkownika");
        helper.setText(activeAccountEmail, true);

        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String to, String token) throws MessagingException {
        String resetPasswordLink = websiteAddress + "auth/resetPassword?token=" + token;

        String resetPasswordEmail = "<html>"
                + "Cześć!<br><br>" +
                "Otrzymaliśmy prośbę o zresetowanie hasła do Twojego konta serwisu Dawka Śmiechu. Aby ustawić nowe hasło, kliknij w poniższy link:<br><br>" +
                "<a href=\""+resetPasswordLink+"\" style=\"color: blue\">Zresetuj hasło</a>" +
                "<br><br>" +
                "Jeśli powyższy link nie działa, skopiuj go i wklej do przeglądarki:<br>" +
                resetPasswordLink +
                "<br><br>" +
                "Link do resetu hasła będzie aktywny przez 24 godziny. Jeśli to nie Ty prosiłeś o zresetowanie hasła, możesz zignorować tę wiadomość – Twoje hasło pozostanie bez zmian.<br>" +
                "<br>" +
                "Pozdrawiamy,<br>" +
                "Zespół DawkaŚmiechu"
                + "</html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom("DawkaSmiechu@" + websiteDomain);
        helper.setSubject("Resetowanie hasła");
        helper.setText(resetPasswordEmail, true);

        mailSender.send(message);
    }
}
