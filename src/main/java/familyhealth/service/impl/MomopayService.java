package familyhealth.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import familyhealth.configuration.MomoConfig;
import familyhealth.model.dto.request.PaymentRequestDTO;
import familyhealth.model.dto.response.PaymentResponseDTO;
import familyhealth.service.IMomopayService;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class MomopayService implements IMomopayService {
    private final MomoConfig config;

    public MomopayService(MomoConfig config) {
        this.config = config;
    }

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO, HttpServletRequest request) {
        try {
            Map<String, String> params = new HashMap<>();
            String orderId = String.valueOf(System.currentTimeMillis());
            params.put("partnerCode", config.getPartnerCode());
            params.put("accessKey", config.getAccessKey());
            params.put("requestId", orderId);
            params.put("amount", String.valueOf(requestDTO.getAmount()));
            params.put("orderId", orderId);
            params.put("orderInfo", requestDTO.getOrderInfo() == null ? "Payment" : requestDTO.getOrderInfo());
            params.put("returnUrl", config.getReturnUrl());
            params.put("notifyUrl", config.getReturnUrl());
            params.put("extraData", "");

            // build raw string
            List<String> keys = new ArrayList<>(params.keySet());
            Collections.sort(keys);
            StringBuilder raw = new StringBuilder();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String k = it.next();
                raw.append(k).append("=").append(params.get(k));
                if (it.hasNext())
                    raw.append('&');
            }

            String signature = hmacSHA256(config.getSecretKey(), raw.toString());
            StringBuilder query = new StringBuilder();
            for (Iterator<String> itr = keys.iterator(); itr.hasNext();) {
                String k = itr.next();
                String v = params.get(k);
                query.append(URLEncoder.encode(k, StandardCharsets.UTF_8)).append("=")
                        .append(URLEncoder.encode(v, StandardCharsets.UTF_8));
                if (itr.hasNext())
                    query.append('&');
            }
            query.append("&signature=").append(URLEncoder.encode(signature, StandardCharsets.UTF_8));

            String paymentUrl = config.getPayUrl() + "?" + query.toString();

            return PaymentResponseDTO.builder()
                    .status("Ok")
                    .message("MoMo URL created")
                    .url(paymentUrl)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("MoMo create payment error", e);
        }
    }

    private String hmacSHA256(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash)
                sb.append(String.format("%02x", b & 0xff));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateCallbackSignature(HttpServletRequest request) {
        try {
            Map<String, String> params = new HashMap<>();
            var names = request.getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                params.put(name, request.getParameter(name));
            }

            String signature = params.remove("signature");
            if (signature == null)
                return false;

            List<String> keys = new ArrayList<>(params.keySet());
            Collections.sort(keys);
            StringBuilder raw = new StringBuilder();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String k = it.next();
                raw.append(k).append("=").append(params.get(k));
                if (it.hasNext())
                    raw.append('&');
            }

            String calc = hmacSHA256(config.getSecretKey(), raw.toString());
            return signature.equals(calc);
        } catch (Exception e) {
            return false;
        }
    }
}
