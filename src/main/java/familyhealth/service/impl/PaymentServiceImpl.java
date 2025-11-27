package familyhealth.service.impl;

import familyhealth.service.IMomopayService;
import familyhealth.service.IPaymentService;
import familyhealth.service.IVnpayService;
import familyhealth.service.IZalopayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private IVnpayService vnPayService;

    @Autowired
    private IMomopayService momoPayService;

    @Autowired
    private IZalopayService zaloPayService;

    @Override
    public String createPayment(String paymentMethod, long amount, String orderInfo, HttpServletRequest request)
            throws Exception {

        switch (paymentMethod.toLowerCase()) {
            case "vnpay":
                return vnPayService.createPaymentUrl(amount, orderInfo);

            case "momo":
                String orderIdMomo = String.valueOf(System.currentTimeMillis());
                return momoPayService.createPayment(amount, orderIdMomo, orderInfo);

            case "zalopay":
                String transIdZalo = new SimpleDateFormat("yyMMdd").format(new Date()) + "_"
                        + System.currentTimeMillis();
                return zaloPayService.createOrder(amount, transIdZalo);

            default:
                throw new Exception("Phương thức thanh toán không hợp lệ: " + paymentMethod);
        }
    }
}