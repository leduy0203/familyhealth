<<<<<<< HEAD:src/main/java/familyhealth/model/dto/PaymentDTO.java
package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;
import familyhealth.common.PaymentMethod;
import familyhealth.common.PaymentStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    @NotNull(message = "Payment method không được null")
    @JsonProperty("payment_method")
    private PaymentMethod method;

    @NotNull(message = "Payment status không được null")
    @JsonProperty("status")
    private PaymentStatus status;

    @JsonProperty("transaction_code")
    private String transactionCode;

    @JsonProperty("paid_at")
    private LocalDateTime paidAt;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @NotNull(message = "Medical result ID không được null")
    @JsonProperty("medical_result_id")
    private Long medicalResultId;
}
=======
package vn.familyhealth.model.dto;

public class PaymentDTO {
}
>>>>>>> f3ef2a1816e0a6ceed82d37f450c23fd4ce4c822:src/main/java/vn/familyhealth/model/dto/PaymentDTO.java
