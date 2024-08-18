package com.coresaken.JokeApp.auth.controller;

import com.coresaken.JokeApp.auth.dto.request.ResetPasswordDto;
import com.coresaken.JokeApp.auth.dto.request.SignInRequestDto;
import com.coresaken.JokeApp.auth.dto.request.SignUpRequestDto;
import com.coresaken.JokeApp.auth.dto.response.AuthenticationResponse;
import com.coresaken.JokeApp.auth.service.ActiveAccountService;
import com.coresaken.JokeApp.auth.service.AuthenticationService;
import com.coresaken.JokeApp.auth.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    final AuthenticationService service;

    final ActiveAccountService activeAccountService;
    final ResetPasswordService resetPasswordService;

    @PostMapping("/signUp")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody SignUpRequestDto request){
        return service.signUp(request);
    }

    @PostMapping("/signIn")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody SignInRequestDto request){
        return service.signIn(request);
    }

    @PostMapping("/active/{code}")
    public void activeAccount(@PathVariable("code") String code){
        activeAccountService.activeAccount(code);
    }

    @PostMapping("/requireResetPassword")
    public ResponseEntity<AuthenticationResponse> requireResetPassword(@RequestBody String email) {
        return resetPasswordService.requireResetPassword(email);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<AuthenticationResponse> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return resetPasswordService.resetPassword(resetPasswordDto);
    }
}
