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