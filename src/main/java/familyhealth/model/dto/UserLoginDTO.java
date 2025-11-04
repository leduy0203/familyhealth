package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank(message = "Phone không được để trống")
    @Size(max = 10, message = "Phone tối đa 10 ký tự")
    @JsonProperty("phone")
    private String phone;

    @NotBlank(message = "Password không được để trống")
    @JsonProperty("password")
    private String password;
}
