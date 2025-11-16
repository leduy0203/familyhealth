package familyhealth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import familyhealth.model.dto.request.PaymentRequestDTO;
import familyhealth.model.dto.response.PaymentResponseDTO;
import familyhealth.service.impl.VnpayService;
import familyhealth.service.IZalopayService;
import familyhealth.service.IMomopayService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final VnpayService vnpayService;
    private final IZalopayService zalopayService;
    private final IMomopayService momopayService;

    @Value("${frontend.url:http://localhost:3000}")
    private String frontendUrl;

    public PaymentController(VnpayService vnpayService, IZalopayService zalopayService,
            IMomopayService momopayService) {
        this.vnpayService = vnpayService;
        this.zalopayService = zalopayService;
        this.momopayService = momopayService;
    }

    @PostMapping("/create-payment")
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @RequestBody PaymentRequestDTO requestDTO,
            HttpServletRequest request) {
        try {
            if (requestDTO == null) {
                logger.warn("Payment request DTO is null");
                return ResponseEntity.badRequest().build();
            }

            logger.info("Creating payment");
            PaymentResponseDTO response = vnpayService.createPayment(requestDTO, request);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating payment", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/create-zalopay")
    public ResponseEntity<PaymentResponseDTO> createZalopay(
            @RequestBody PaymentRequestDTO requestDTO,
            HttpServletRequest request) {
        try {
            if (requestDTO == null)
                return ResponseEntity.badRequest().build();
            PaymentResponseDTO response = zalopayService.createPayment(requestDTO, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating ZaloPay payment", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/zalopay-callback")
    public RedirectView zalopayCallback(HttpServletRequest request) {
        try {
            logger.info("ZaloPay callback received");
            if (request == null)
                return new RedirectView(frontendUrl + "/payment/failure?errorCode=NULL_REQUEST");
            boolean isValid = zalopayService.validateCallbackSignature(request);
            String frontendSuccessUrl = frontendUrl + "/payment/success";
            String frontendFailureUrl = frontendUrl + "/payment/failure";
            if (!isValid)
                return new RedirectView(frontendFailureUrl + "?errorCode=INVALID_SIGNATURE");
            String returnCode = request.getParameter("return_code");
            String transId = request.getParameter("app_trans_id");
            if ("1".equals(returnCode)) {
                return new RedirectView(frontendSuccessUrl + "?orderId=" + transId);
            } else {
                return new RedirectView(frontendFailureUrl + "?errorCode=" + returnCode);
            }
        } catch (Exception e) {
            logger.error("Error processing ZaloPay callback", e);
            return new RedirectView(frontendUrl + "/payment/failure?errorCode=INTERNAL_ERROR");
        }
    }

    @PostMapping("/create-momo")
    public ResponseEntity<PaymentResponseDTO> createMomo(
            @RequestBody PaymentRequestDTO requestDTO,
            HttpServletRequest request) {
        try {
            if (requestDTO == null)
                return ResponseEntity.badRequest().build();
            PaymentResponseDTO response = momopayService.createPayment(requestDTO, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating MoMo payment", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/momo-callback")
    public RedirectView momoCallback(HttpServletRequest request) {
        try {
            logger.info("MoMo callback received");
            if (request == null)
                return new RedirectView(frontendUrl + "/payment/failure?errorCode=NULL_REQUEST");
            boolean isValid = momopayService.validateCallbackSignature(request);
            String frontendSuccessUrl = frontendUrl + "/payment/success";
            String frontendFailureUrl = frontendUrl + "/payment/failure";
            if (!isValid)
                return new RedirectView(frontendFailureUrl + "?errorCode=INVALID_SIGNATURE");
            String resultCode = request.getParameter("resultCode");
            String orderId = request.getParameter("orderId");
            if ("0".equals(resultCode)) {
                return new RedirectView(frontendSuccessUrl + "?orderId=" + orderId);
            } else {
                return new RedirectView(frontendFailureUrl + "?errorCode=" + resultCode);
            }
        } catch (Exception e) {
            logger.error("Error processing MoMo callback", e);
            return new RedirectView(frontendUrl + "/payment/failure?errorCode=INTERNAL_ERROR");
        }
    }

    @GetMapping("/vnpay-callback")
    public RedirectView vnpayCallback(HttpServletRequest request) {
        try {
            logger.info("VNPay callback received");

            if (request == null) {
                logger.error("HttpServletRequest is null");
                return new RedirectView(frontendUrl + "/payment/failure?errorCode=NULL_REQUEST");
            }

            boolean isValid = vnpayService.validateCallbackSignature(request);

            String frontendSuccessUrl = frontendUrl + "/payment/success";
            String frontendFailureUrl = frontendUrl + "/payment/failure";

            if (isValid) {
                String responseCode = request.getParameter("vnp_ResponseCode");
                String txnRef = request.getParameter("vnp_TxnRef");

                if (responseCode == null || txnRef == null) {
                    logger.warn("Missing required callback parameters");
                    return new RedirectView(frontendFailureUrl + "?errorCode=MISSING_PARAMS");
                }

                if ("00".equals(responseCode)) {
                    logger.info("Payment successful for transaction: {}", txnRef);
                    return new RedirectView(frontendSuccessUrl + "?orderId=" + txnRef);
                } else {
                    logger.warn("Payment failed with response code: {}", responseCode);
                    return new RedirectView(frontendFailureUrl + "?errorCode=" + responseCode);
                }
            } else {
                logger.error("Invalid VNPay callback signature");
                return new RedirectView(frontendFailureUrl + "?errorCode=INVALID_SIGNATURE");
            }
        } catch (Exception e) {
            logger.error("Error processing VNPay callback", e);
            return new RedirectView(frontendUrl + "/payment/failure?errorCode=INTERNAL_ERROR");
        }
    }
}
