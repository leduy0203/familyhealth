package familyhealth.controller;

import familyhealth.model.dto.request.LogoutRequest;
import familyhealth.model.dto.request.SignInRequest;
import familyhealth.model.dto.response.ApiResponse;
import familyhealth.model.dto.response.RefreshTokenResponse;
import familyhealth.model.dto.response.SignInResponse;
import familyhealth.service.impl.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final AuthenticationService authenticationService;
    
    @PostMapping("/sign-in")
    ApiResponse<SignInResponse> signIn(@RequestBody @Valid SignInRequest request,
                                       HttpServletResponse response) {
        var result = authenticationService.signIn(request, response);
        return ApiResponse.<SignInResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Sign in success")
                .data(result)
                .build();
    }

    @PostMapping("/refresh-token")
    ApiResponse<RefreshTokenResponse> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) {
        var result = authenticationService.refreshToken(refreshToken);
        return ApiResponse.<RefreshTokenResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Refreshed token success")
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest request, HttpServletResponse response) {
        authenticationService.signOut(request, response);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Sign out success")
                .build();
    }
}
