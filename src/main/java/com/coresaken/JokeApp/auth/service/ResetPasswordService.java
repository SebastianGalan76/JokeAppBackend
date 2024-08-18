package com.coresaken.JokeApp.auth.service;

import com.coresaken.JokeApp.auth.dto.request.ResetPasswordDto;
import com.coresaken.JokeApp.auth.dto.response.AuthenticationResponse;
import com.coresaken.JokeApp.auth.util.ErrorResponse;
import com.coresaken.JokeApp.auth.util.RandomToken;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.database.model.ResetPasswordToken;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.repository.ResetPasswordTokenRepository;
import com.coresaken.JokeApp.database.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    final EmailSenderService emailSenderService;

    final PasswordEncoder passwordEncoder;

    final UserRepository userRepository;
    final ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Async
    public ResponseEntity<AuthenticationResponse> requireResetPassword(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ErrorResponse.build(1, "There is no user with provided email");
        }

        String token = RandomToken.getToken();
        ResetPasswordToken resetToken = resetPasswordTokenRepository.findByUserId(user.getId())
                .orElse(null);

        if (resetToken != null) {
            resetToken.setToken(token);
            resetToken.setExpiredAt(LocalDateTime.now().plusMinutes(10));
        } else {
            resetToken = new ResetPasswordToken(user, token);
        }

        resetPasswordTokenRepository.save(resetToken);

        try {
            emailSenderService.sendResetPasswordEmail(email, token);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        AuthenticationResponse response = new AuthenticationResponse();
        response.setStatus(ResponseStatusEnum.SUCCESS);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<AuthenticationResponse> resetPassword(ResetPasswordDto resetPasswordDto) {
        ResetPasswordToken resetToken = resetPasswordTokenRepository.findByToken(resetPasswordDto.getToken()).orElse(null);

        if (resetToken == null || resetToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            if (resetToken != null) {
                resetPasswordTokenRepository.delete(resetToken);
            }

            return ErrorResponse.build(1, "Your token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));

        userRepository.save(user);
        resetPasswordTokenRepository.delete(resetToken);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setStatus(ResponseStatusEnum.SUCCESS);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
