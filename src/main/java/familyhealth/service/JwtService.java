package familyhealth.service;

import com.nimbusds.jose.JOSEException;
import familyhealth.model.User;
import java.text.ParseException;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    String extractUserName(String accessToken);
    boolean verificationToken(String token, User user) throws ParseException, JOSEException;
    long extractTokenExpired(String token);
}
