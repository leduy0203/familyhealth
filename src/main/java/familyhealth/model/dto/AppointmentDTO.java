package familyhealth.model.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import familyhealth.common.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    @NotNull(message = "Thời gian gặp không được null")
    @JsonProperty("time")
    private LocalDateTime time;

    @NotNull(message = "Trạng thái không được null")
    @JsonProperty("status")
    private AppointmentStatus status;

    @Size(max = 200, message = "Note tối đa 200 ký tự")
    @JsonProperty("note")
    private String note;

    @Size(max = 200, message = "Địa điểm tối đa 200 ký tự")
    @JsonProperty("location")
    private String location;

    @NotNull(message = "Doctor ID không được null")
    @JsonProperty("doctor_id")
    private Long doctorId;

    @NotNull(message = "Member ID không được null")
    @JsonProperty("member_id")
    private Long memberId;
}