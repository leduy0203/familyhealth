package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import familyhealth.common.Gender;

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

    @JsonProperty("fullname")
    private String fullName;

    @NotNull(message = "Gender không được null")
    @JsonProperty("gender")
    private Gender gender;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @Email(message = "Email không hợp lệ")
    @JsonProperty("email")
    private String email;

    @Size(max = 12, message = "CCCD tối đa 12 ký tự")
    @JsonProperty("cccd")
    private String cccd;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("profile_id")
    private Integer profileId;

    @NotNull(message = "Role ID không được null")
    @JsonProperty("role_id")
    private Long roleId;
}
