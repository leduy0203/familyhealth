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
import familyhealth.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(14 * 24 * 60 * 60); // 2 tuần

        response.addCookie(cookie);

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) {
        log.info("refresh token");
        if (StringUtils.isBlank(refreshToken)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        String phone = jwtService.extractUserName(refreshToken);
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        if(!Objects.equals(refreshToken, user.getRefreshToken()) || StringUtils.isBlank(user.getRefreshToken())) {
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

        deleteRefreshTokenCookie(response);
        log.info("Sign out successful for phone: {}", phone);
    }

    private void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
