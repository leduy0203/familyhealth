package familyhealth.model.dto.request;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private long amount;
    private String orderInfo;
    private String bankCode;

}
