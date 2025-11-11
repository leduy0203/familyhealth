package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "Phone không được để trống")
    @Size(max = 10, message = "Phone tối đa 10 ký tự")
    @JsonProperty("phone")
    private String phone;

    @NotBlank(message = "Password không được để trống")
    @JsonProperty("password")
    private String password;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    @JsonProperty("is_active")
    private Boolean isActive;

    @NotNull(message = "Role ID không được null")
    @JsonProperty("role_id")
    private Long roleId;
}
