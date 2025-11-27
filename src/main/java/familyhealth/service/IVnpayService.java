package familyhealth.service;

import java.io.UnsupportedEncodingException;

public interface IVnpayService {
    /**
     * @param amount
     * @param orderInfo
     * @return
     */
    String createPaymentUrl(long amount, String orderInfo) throws UnsupportedEncodingException;
}