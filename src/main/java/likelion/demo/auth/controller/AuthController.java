package likelion.demo.auth.controller;

import jakarta.validation.Valid;
import likelion.demo.auth.dto.request.*;
import likelion.demo.auth.dto.response.*;
import likelion.demo.auth.service.AuthService;
import likelion.demo.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> addMember(@Valid @RequestBody SignupRequest signupRequest){
        SignupResponse signupResponse =
                authService.addMember(signupRequest.getLoginId(), signupRequest.getPassword(), signupRequest.getPhoneNumber(), signupRequest.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "회원 생성에 성공하였습니다.", signupResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request.getLoginId(), request.getPassword());

        return ResponseEntity.ok(ApiResponse.success(200, "로그인 성공", loginResponse));
    }
}
