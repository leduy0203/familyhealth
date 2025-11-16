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

import familyhealth.configuration.ZaloPayConfig;
import familyhealth.model.dto.request.PaymentRequestDTO;
import familyhealth.model.dto.response.PaymentResponseDTO;
import familyhealth.service.IZalopayService;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ZalopayService implements IZalopayService {
    private final ZaloPayConfig config;

    public ZalopayService(ZaloPayConfig config) {
        this.config = config;
    }

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO, HttpServletRequest request) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("app_id", config.getAppId());
            String app_trans_id = String.valueOf(System.currentTimeMillis());
            params.put("app_trans_id", app_trans_id);
            params.put("app_user", "guest");
            params.put("amount", String.valueOf(requestDTO.getAmount()));
            params.put("app_time", String.valueOf(System.currentTimeMillis()));
            params.put("app_trans_desc", requestDTO.getOrderInfo() == null ? "Payment" : requestDTO.getOrderInfo());
            params.put("embed_data", "{}");
            params.put("item", "[]");

            // build raw data string for mac
            List<String> keys = new ArrayList<>(params.keySet());
            Collections.sort(keys);
            StringBuilder raw = new StringBuilder();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String k = it.next();
                String v = params.get(k);
                raw.append(k).append("=").append(v);
                if (it.hasNext())
                    raw.append("|");
            }

            String mac = hmacSHA256(config.getKey1(), raw.toString());
            // create query
            StringBuilder query = new StringBuilder();
            for (Iterator<String> itr = keys.iterator(); itr.hasNext();) {
                String k = itr.next();
                String v = params.get(k);
                query.append(URLEncoder.encode(k, StandardCharsets.UTF_8)).append("=")
                        .append(URLEncoder.encode(v, StandardCharsets.UTF_8));
                if (itr.hasNext())
                    query.append('&');
            }
            query.append("&mac=").append(URLEncoder.encode(mac, StandardCharsets.UTF_8));

            String paymentUrl = config.getPayUrl() + "?" + query.toString();

            return PaymentResponseDTO.builder()
                    .status("Ok")
                    .message("ZaloPay URL created")
                    .url(paymentUrl)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Zalopay create payment error", e);
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

            String mac = params.remove("mac");
            if (mac == null)
                return false;

            List<String> keys = new ArrayList<>(params.keySet());
            Collections.sort(keys);
            StringBuilder raw = new StringBuilder();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String k = it.next();
                raw.append(k).append("=").append(params.get(k));
                if (it.hasNext())
                    raw.append("|");
            }

            String calc = hmacSHA256(config.getKey1(), raw.toString());
            return mac.equals(calc);
        } catch (Exception e) {
            return false;
        }
    }
}
