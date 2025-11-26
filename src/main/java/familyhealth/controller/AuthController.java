package familyhealth.controller;

import familyhealth.model.dto.request.LogoutRequest;
import familyhealth.model.dto.request.RefreshTokenRequest;
import familyhealth.model.dto.request.SignInRequest;
import familyhealth.model.dto.response.ApiResponse;
import familyhealth.model.dto.response.RefreshTokenResponse;
import familyhealth.model.dto.response.SignInResponse;
import familyhealth.service.IAuthService;
import familyhealth.utils.MessageKey;
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
    
    private final IAuthService authenticationService;
    
    @PostMapping("/sign-in")
    ApiResponse<SignInResponse> signIn(@RequestBody @Valid SignInRequest request,
                                       HttpServletResponse response) {

        var result = authenticationService.signIn(request, response);

        return ApiResponse.<SignInResponse>builder()
                .code(HttpStatus.OK.value())
                .message(MessageKey.SIGN_IN_SUCCESS)
                .data(result)
                .build();
    }

    @PostMapping("/refresh-token")
    ApiResponse<RefreshTokenResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshTokenFromCookie,
            @RequestBody(required = false) RefreshTokenRequest request) {

        String refreshToken = (request != null && request.getRefreshToken() != null) 
                ? request.getRefreshToken() 
                : refreshTokenFromCookie;
        
        var result = authenticationService.refreshToken(refreshToken);

        return ApiResponse.<RefreshTokenResponse>builder()
                .code(HttpStatus.OK.value())
                .message(MessageKey.REFRESH_TOKEN_SUCCESS)
                .data(result)
                .build();
    }

    @GetMapping("/logout")
    ApiResponse<Void> logout(HttpServletResponse response) {

        authenticationService.signOut(response);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(MessageKey.LOGOUT_SUCCESS)
                .build();
    }
}
