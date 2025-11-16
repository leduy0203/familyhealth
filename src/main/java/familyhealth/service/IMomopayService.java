package familyhealth.service;

import familyhealth.model.dto.request.PaymentRequestDTO;
import familyhealth.model.dto.response.PaymentResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface IMomopayService {
    PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO, HttpServletRequest request);

    boolean validateCallbackSignature(HttpServletRequest request);
}
