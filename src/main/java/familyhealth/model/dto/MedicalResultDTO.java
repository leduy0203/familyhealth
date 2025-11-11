package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalResultDTO {
    @NotBlank(message = "Ghi chú không được để trống")
    @Size(max = 100, message = "Tên kết quả khám tối đa 100 ký tự")
    @JsonProperty("name")
    private String name;

    @Size(max = 200, message = "Ghi chú tối đa 200 ký tự")
    @JsonProperty("note")
    private String note;

    @Size(max = 200, message = "Chuẩn đoán tối đa 200 ký tự")
    @JsonProperty("diagnose")
    private String diagnose;

    @NotNull(message = "Tổng tiền không được null")
    @PositiveOrZero(message = "Tổng tiền phải >= 0")
    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @NotNull(message = "Appointment ID không được null")
    @JsonProperty("appointment_id")
    private Long appointmentId;
}
