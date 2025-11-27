package familyhealth.service.impl;

import familyhealth.service.IMomopayService;
import familyhealth.utils.HmacUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class MomopayService implements IMomopayService {

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.endpoint}")
    private String endpoint;

    @Value("${momo.returnUrl}")
    private String returnUrl;

    @Value("${momo.ipnUrl}")
    private String ipnUrl;

    @Override
    public String createPayment(long amount, String orderId, String orderInfo) throws Exception {
        String requestId = String.valueOf(System.currentTimeMillis());
        String requestType = "captureWallet";
        String extraData = "";

        String rawSignature = "accessKey=" + accessKey + "&amount=" + amount + "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl + "&orderId=" + orderId + "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode + "&redirectUrl=" + returnUrl +
                "&requestId=" + requestId + "&requestType=" + requestType;

        String signature = HmacUtil.HmacSHA256(rawSignature, secretKey);

        JSONObject message = new JSONObject();
        message.put("partnerCode", partnerCode);
        message.put("partnerName", "FamilyHealth Payment");
        message.put("storeId", "FamilyHealthStore");
        message.put("requestId", requestId);
        message.put("amount", amount);
        message.put("orderId", orderId);
        message.put("orderInfo", orderInfo);
        message.put("redirectUrl", returnUrl);
        message.put("ipnUrl", ipnUrl);
        message.put("lang", "vi");
        message.put("extraData", extraData);
        message.put("requestType", requestType);
        message.put("signature", signature);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(endpoint);
            StringEntity stringEntity = new StringEntity(message.toString(), StandardCharsets.UTF_8);
            post.setHeader("content-type", "application/json");
            post.setEntity(stringEntity);

            try (CloseableHttpResponse response = client.execute(post)) {
                String result = EntityUtils.toString(response.getEntity());
                JSONObject jsonResult = new JSONObject(result);

                if (jsonResult.has("payUrl")) {
                    return jsonResult.getString("payUrl");
                } else {
                    throw new Exception("MoMo Error: " + result);
                }
            }
        }
    }

    @Override
    public String handlePaymentResponse(HttpServletRequest request) {
        try {
            String resultCode = request.getParameter("resultCode");
            String message = request.getParameter("message");

            if ("0".equals(resultCode)) {
                return "Giao dịch thành công: " + message;
            } else {
                return "Giao dịch thất bại: " + message;
            }
        } catch (Exception e) {
            return "Lỗi xử lý: " + e.getMessage();
        }
    }
}