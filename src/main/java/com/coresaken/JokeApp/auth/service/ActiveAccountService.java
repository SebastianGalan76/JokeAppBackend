package com.coresaken.JokeApp.auth.service;

import com.coresaken.JokeApp.auth.util.RandomToken;
import com.coresaken.JokeApp.database.model.ActiveAccountToken;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.repository.ActiveAccountTokenRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActiveAccountService {
    final EmailSenderService emailSenderService;

    final ActiveAccountTokenRepository activeAccountTokenRepository;

    @Async
    public void processAccountActivation(User user) {
        String activeAccountToken = RandomToken.getToken();

        try {
            activeAccountTokenRepository.save(new ActiveAccountToken(user.getId(), activeAccountToken));
            emailSenderService.sendActiveAccountEmail(user.getEmail(), activeAccountToken);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean isAccountActivated(User user){
        return activeAccountTokenRepository.findByUserId(user.getId()).isEmpty();
    }
    public void activeAccount(String code){
        activeAccountTokenRepository.deleteByCode(code);
    }
}
