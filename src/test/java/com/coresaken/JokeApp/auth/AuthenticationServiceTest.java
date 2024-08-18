package com.coresaken.JokeApp.auth;

import com.coresaken.JokeApp.auth.dto.request.SignInRequestDto;
import com.coresaken.JokeApp.auth.dto.request.SignUpRequestDto;
import com.coresaken.JokeApp.auth.dto.response.AuthenticationResponse;
import com.coresaken.JokeApp.auth.service.ActiveAccountService;
import com.coresaken.JokeApp.auth.service.AuthenticationService;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ActiveAccountService activeAccountService;

    @InjectMocks
    private AuthenticationService authenticationService;

    public AuthenticationServiceTest() {
        openMocks(this);
    }

    @Test
    public void testSignUp_LoginTooLong() {
        SignUpRequestDto request = new SignUpRequestDto("tooLongLogin-------------------", "test@domain.com", "123qwe");
        ResponseEntity<AuthenticationResponse> response = authenticationService.signUp(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseStatusEnum.ERROR, response.getBody().getStatus());
        assertEquals(1, response.getBody().getError().getCode());
    }

    @Test
    public void testSignUp_LoginTooShort() {
        SignUpRequestDto request = new SignUpRequestDto("abc", "test@domain.com", "123qwe");
        ResponseEntity<AuthenticationResponse> response = authenticationService.signUp(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseStatusEnum.ERROR, response.getBody().getStatus());
        assertEquals(2, response.getBody().getError().getCode());
    }

    @Test
    public void testSignUp_PasswordTooShort() {
        SignUpRequestDto request = new SignUpRequestDto("validLogin", "test@domain.com", "abc");
        ResponseEntity<AuthenticationResponse> response = authenticationService.signUp(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseStatusEnum.ERROR, response.getBody().getStatus());
        assertEquals(3, response.getBody().getError().getCode());
    }

    @Test
    public void testSignUp_EmailTooLong() {
        SignUpRequestDto request = new SignUpRequestDto("validLogin", "toLongEmail-----------------------------------------------------------------------", "123qwe");
        ResponseEntity<AuthenticationResponse> response = authenticationService.signUp(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseStatusEnum.ERROR, response.getBody().getStatus());
        assertEquals(4, response.getBody().getError().getCode());
    }

    @Test
    public void testSignUp_LoginOrEmailAlreadyTaken() {
        when(userRepository.findByEmailOrLogin("test@domain.com", "validLogin")).thenReturn(Optional.of(new User()));

        SignUpRequestDto request = new SignUpRequestDto("validLogin", "test@domain.com", "123qwe");
        ResponseEntity<AuthenticationResponse> response = authenticationService.signUp(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseStatusEnum.ERROR, response.getBody().getStatus());
        assertEquals(5, response.getBody().getError().getCode());
    }

    @Test
    public void testSignUp_Success() {
        SignUpRequestDto request = new SignUpRequestDto("validLogin", "test@example.com", "123qwe");

        when(userRepository.findByEmailOrLogin("test@domain.com", "validLogin")).thenReturn(Optional.empty());

        User savedUser = new User();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        ResponseEntity<AuthenticationResponse> response = authenticationService.signUp(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseStatusEnum.SUCCESS, response.getBody().getStatus());
    }

    @Test
    public void testSignIn_NoUser(){
        SignInRequestDto request = new SignInRequestDto("validLogin", "123qwe");
        when(userRepository.findByEmailOrLogin("test@domain.com", "validLogin")).thenReturn(Optional.empty());

        ResponseEntity<AuthenticationResponse> response = authenticationService.signIn(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseStatusEnum.ERROR, response.getBody().getStatus());
        assertEquals(1, response.getBody().getError().getCode());
    }

    @Test
    public void testSignIn_InvalidPassword(){
        SignInRequestDto request = new SignInRequestDto("validLogin", "wrongPassword");

        User user = new User();
        user.setPassword("123qwe");

        when(userRepository.findByEmailOrLogin("validLogin", "validLogin")).thenReturn(Optional.of(new User()));
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        ResponseEntity<AuthenticationResponse> response = authenticationService.signIn(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseStatusEnum.ERROR, response.getBody().getStatus());
        assertEquals(2, response.getBody().getError().getCode());
    }

    @Test
    public void testSignIn_InactiveAccount() {
        SignInRequestDto request = new SignInRequestDto("test@domain.com", "123qwe");

        User user = new User();
        user.setPassword("123qwe");

        when(userRepository.findByEmailOrLogin("test@domain.com", "test@domain.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123qwe", user.getPassword())).thenReturn(true);
        when(activeAccountService.isAccountActivated(user)).thenReturn(true);

        ResponseEntity<AuthenticationResponse> response = authenticationService.signIn(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseStatusEnum.ERROR, response.getBody().getStatus());
        assertEquals(3, response.getBody().getError().getCode());
    }
}
