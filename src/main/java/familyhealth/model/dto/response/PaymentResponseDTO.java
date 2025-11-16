package familyhealth.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponseDTO {
    private String status;
    private String message;
    private String url;
}
