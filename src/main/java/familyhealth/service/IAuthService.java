package familyhealth.service;

import familyhealth.model.dto.request.LogoutRequest;
import familyhealth.model.dto.request.SignInRequest;
import familyhealth.model.dto.response.RefreshTokenResponse;
import familyhealth.model.dto.response.SignInResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;

public interface IAuthService {
    SignInResponse signIn(SignInRequest request, HttpServletResponse response);
    RefreshTokenResponse refreshToken(@CookieValue(name = "refreshToken") String refreshToken);
    void signOut(LogoutRequest request, HttpServletResponse response);
}
