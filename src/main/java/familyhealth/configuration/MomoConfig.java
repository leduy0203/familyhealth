package familyhealth.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class MomoConfig {
    @Value("${momopay.partner_code}")
    private String partnerCode;
    @Value("${momopay.access_key}")
    private String accessKey;
    @Value("${momopay.secret_key}")
    private String secretKey;
    @Value("${momopay.pay_url}")
    private String payUrl;
    @Value("${momopay.return_url}")
    private String returnUrl;
}
