package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import familyhealth.common.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    @JsonProperty("fullname")
    private String fullname;

    @NotBlank(message = "Address không được để trống")
    @Size(max = 200, message = "Address tối đa 200 ký tự")
    @JsonProperty("address")
    private String address;

    @NotNull(message = "Gender không được null")
    @JsonProperty("gender")
    private Gender gender;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate dateOfBirth;

    @Email(message = "Email không hợp lệ")
    @JsonProperty("email")
    private String email;

    @Size(max = 12, message = "CCCD tối đa 12 ký tự")
    private String idCard;
}
