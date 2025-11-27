//package familyhealth.service.impl;
//
//import familyhealth.service.IZalopayService;
//import familyhealth.utils.HmacUtil;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Service
//public class ZaloPayService implements IZalopayService {
//
//    @Value("${zalopay.appId}")
//    private String appId;
//
//    @Value("${zalopay.key1}")
//    private String key1;
//
//    @Value("${zalopay.endpoint}")
//    private String endpoint;
//
//    @Value("${zalopay.callbackUrl}")
//    private String callbackUrl;
//
//    @Override
//    public String createOrder(long amount, String transId) throws Exception {
//        final Map<String, Object> embed_data = new HashMap<String, Object>() {
//            {
//                put("redirecturl", callbackUrl);
//            }
//        };
//
//        final Map<String, Object>[] item = new Map[] {};
//
//        Map<String, Object> order = new HashMap<String, Object>() {
//            {
//                put("app_id", appId);
//                put("app_user", "FamilyHealthUser");
//                put("app_time", System.currentTimeMillis());
//                put("amount", amount);
//                put("app_trans_id", transId);
//                put("embed_data", new JSONObject(embed_data).toString());
//                put("item", new JSONArray(item).toString());
//                put("description", "FamilyHealth - Payment for #" + transId);
//                put("bank_code", "zalopayapp");
//            }
//        };
//
//        String data = order.get("app_id") + "|" + order.get("app_trans_id") + "|" + order.get("app_user") + "|"
//                + order.get("amount")
//                + "|" + order.get("app_time") + "|" + order.get("embed_data") + "|" + order.get("item");
//
//        order.put("mac", HmacUtil.HmacSHA256(data, key1));
//
//        try (CloseableHttpClient client = HttpClients.createDefault()) {
//            HttpPost post = new HttpPost(endpoint);
//
//            List<NameValuePair> params = new ArrayList<>();
//            for (Map.Entry<String, Object> e : order.entrySet()) {
//                params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
//            }
//            post.setEntity(new UrlEncodedFormEntity(params));
//
//            try (CloseableHttpResponse res = client.execute(post)) {
//                BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
//                StringBuilder resultJsonStr = new StringBuilder();
//                String line;
//                while ((line = rd.readLine()) != null) {
//                    resultJsonStr.append(line);
//                }
//
//                JSONObject jsonResult = new JSONObject(resultJsonStr.toString());
//                if (jsonResult.has("order_url")) {
//                    return jsonResult.getString("order_url");
//                } else {
//                    throw new Exception("ZaloPay Error: " + jsonResult.toString());
//                }
//            }
//        }
//    }
//}