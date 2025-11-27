package familyhealth.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class VnpayConfig {
    @Value("${vnpay.pay_url}")
    private String vnp_PayUrl;
    @Value("${vnpay.return_url}")
    private String vnp_ReturnUrl;
    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;
    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    public static final String VNP_VERSION = "2.1.0";
    public static final String VNP_COMMAND = "pay";
    public static final String VNP_CURRCODE = "VND";
    public static final String VNP_LOCALE = "vn";
    public static final String VNP_ORDERTYPE = "other";
}
