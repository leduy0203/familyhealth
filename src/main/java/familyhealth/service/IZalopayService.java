package familyhealth.service;

public interface IZalopayService {
    /**
     * @param amount  Số tiền cần thanh toán
     * @param transId Mã giao dịch (Format: yyMMdd_xxxx)
     * @return URL thanh toán
     */
    String createOrder(long amount, String transId) throws Exception;
}