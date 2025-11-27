package familyhealth.service;

import jakarta.servlet.http.HttpServletRequest;

public interface IMomopayService {
    String createPayment(long amount, String orderId, String orderInfo) throws Exception;

    String handlePaymentResponse(HttpServletRequest request);
}