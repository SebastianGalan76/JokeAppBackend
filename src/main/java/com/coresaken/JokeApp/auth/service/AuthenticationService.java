package com.coresaken.JokeApp.auth.service;

import com.coresaken.JokeApp.auth.dto.request.SignInRequestDto;
import com.coresaken.JokeApp.auth.dto.request.SignUpRequestDto;
import com.coresaken.JokeApp.auth.dto.response.AuthenticationResponse;
import com.coresaken.JokeApp.auth.util.ErrorResponse;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    final JwtService jwtService;
    final ActiveAccountService activeAccountService;

    final UserRepository userRepository;

    final PasswordEncoder passwordEncoder;
    final AuthenticationManager authenticationManager;
    final EmailSenderService emailSenderService;

    @Transactional
    public ResponseEntity<AuthenticationResponse> signUp(SignUpRequestDto request) {
        String login = request.getLogin().trim();
        String email = request.getEmail().trim();
        String password = request.getPassword().trim();

        //Validate provided login
        if (login.length() > 30) {
            return ErrorResponse.build(1, "Login jest zbyt długi");
        }
        if (login.length() < 4) {
            return ErrorResponse.build(2, "Login jest zbyt krótki");
        }

        //Validate provided password4
        if (password.length() < 4) {
            return ErrorResponse.build(3, "Hasło jest zbyt krótkie");
        }

        //Validate provided email
        if (email.length() > 80) {
            return ErrorResponse.build(4, "E-mail jest zbyt długi");
        }

        User savedUser = userRepository.findByEmailOrLogin(email, login).orElse(null);
        if (savedUser != null) {
            return ErrorResponse.build(5, "Login lub e-mail jest już zajęty");
        }

        User user = User.builder()
                .login(request.getLogin())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .build();

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            return ErrorResponse.build(6, "Login lub e-mail jest już zajęty");
        }

        activeAccountService.processAccountActivation(user);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setStatus(ResponseStatusEnum.SUCCESS);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<AuthenticationResponse> signIn(SignInRequestDto request) {
        String identifier = request.getIdentifier();

        User user = userRepository.findByEmailOrLogin(identifier, identifier).orElse(null);
        if (user == null) {
            return ErrorResponse.build(1, "Incorrect login details");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ErrorResponse.build(2, "Incorrect login details");
        }

        if (!activeAccountService.isAccountActivated(user)) {
            return ErrorResponse.build(3, "This account is not active yet");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                identifier,
                request.getPassword()
        ));

        AuthenticationResponse response = new AuthenticationResponse();
        response.setStatus(ResponseStatusEnum.SUCCESS);
        response.setJwtToken(jwtService.generateToken(user));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        new CookieClearingLogoutHandler("jwt_token").logout(request, response, null);
    }
}
