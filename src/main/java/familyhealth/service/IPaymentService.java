package familyhealth.service;

import jakarta.servlet.http.HttpServletRequest;

public interface IPaymentService {
    /**
     * @param paymentMethod:
     * @param amount:
     * @param orderInfo:
     * @param request:
     * @return
     */
    String createPayment(String paymentMethod, long amount, String orderInfo, HttpServletRequest request)
            throws Exception;
}