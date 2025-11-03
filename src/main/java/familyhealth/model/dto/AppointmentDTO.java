<<<<<<< HEAD:src/main/java/familyhealth/model/dto/AppointmentDTO.java
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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    @NotNull(message = "Thời gian gặp không được null")
    @JsonProperty("time")
    private LocalDate time;

    @NotNull(message = "Trạng thái không được null")
    @JsonProperty("status")
    private AppointmentStatus status;

    @NotBlank(message = "Ghi chú không được để trống")
    @Size(max = 200, message = "Note tối đa 200 ký tự")
    @JsonProperty("note")
    private String note;

    @NotNull(message = "Doctor ID không được null")
    @JsonProperty("doctor_id")
    private Long doctorId;

    @NotNull(message = "Member ID không được null")
    @JsonProperty("member_id")
    private Long memberId;
=======
package vn.familyhealth.model.dto;

public class AppointmentDTO {
>>>>>>> f3ef2a1816e0a6ceed82d37f450c23fd4ce4c822:src/main/java/vn/familyhealth/model/dto/AppointmentDTO.java
}
