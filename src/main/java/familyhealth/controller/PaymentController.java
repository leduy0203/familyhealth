package familyhealth.controller;

import familyhealth.service.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    @GetMapping("/create")
    public ResponseEntity<?> createPayment(
            @RequestParam String method, // vnpay, momo, zalopay
            @RequestParam long amount,
            @RequestParam String orderInfo,
            HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            String payUrl = paymentService.createPayment(method, amount, orderInfo, request);

            response.put("status", "OK");
            response.put("url", payUrl);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "FAILED");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}