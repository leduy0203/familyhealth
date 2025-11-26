package familyhealth.service.impl;

import com.nimbusds.jose.JOSEException;
import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.model.User;
import familyhealth.model.dto.request.LogoutRequest;
import familyhealth.model.dto.request.SignInRequest;
import familyhealth.model.dto.response.RefreshTokenResponse;
import familyhealth.model.dto.response.SignInResponse;
import familyhealth.repository.UserRepository;
import familyhealth.service.IAuthService;
import familyhealth.service.IJwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationService implements IAuthService {
    
    private final UserRepository userRepository;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    @Value("${jwt.expiration}") // 14 days default
    private long refreshTokenExpiration;

    @Override
    public SignInResponse signIn(SignInRequest request, HttpServletResponse response) {
        log.info("Authentication start ...!");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));

        User user = (User) authentication.getPrincipal();
        log.info("Authority: {}", user.getAuthorities());

        final String accessToken = jwtService.generateAccessToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        ResponseCookie cookie = createRefreshTokenCookie(refreshToken, true);
        response.addHeader("Set-Cookie", cookie.toString());

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) {

        if (StringUtils.isBlank(refreshToken)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String phone = jwtService.extractUserName(refreshToken);
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean check = refreshToken.equals(user.getRefreshToken());

        if(!check || StringUtils.isBlank(user.getRefreshToken())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        try {
            boolean isValidToken = jwtService.verificationToken(refreshToken, user);
            if (!isValidToken) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
            String accessToken = jwtService.generateAccessToken(user);
            log.info("refresh token success");
            return RefreshTokenResponse.builder()
                    .accessToken(accessToken)
                    .userId(user.getId())
                    .build();
        } catch (ParseException | JOSEException e) {
            log.error("Error while refresh token", e);
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public void signOut(LogoutRequest request, HttpServletResponse response) {
        String phone = jwtService.extractUserName(request.getAccessToken());
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setRefreshToken(null);
        userRepository.save(user);

        ResponseCookie cookie = createRefreshTokenCookie(null, false);
        response.addHeader("Set-Cookie", cookie.toString());
        log.info("Sign out successful for phone: {}", phone);
    }

    private ResponseCookie createRefreshTokenCookie(String token, boolean isCreate) {
        return ResponseCookie.from("refreshToken", isCreate ? token : "")
                .httpOnly(true)
                .secure(false) 
                .path("/familyhealth") 
                .sameSite("Lax") 
                .maxAge(isCreate ? refreshTokenExpiration : 0)
                .build();
    }
}
