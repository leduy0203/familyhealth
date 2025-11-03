package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import familyhealth.common.Expertise;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO {
    @NotNull(message = "Chuyên ngành không được null")
    @JsonProperty("expertise")
    private Expertise expertise;

    @NotBlank(message = "Bio không được để trống")
    @Size(max = 255, message = "Bio tối đa 255 ký tự")
    @JsonProperty("bio")
    private String bio;

    @NotNull(message = "User ID không được null")
    @JsonProperty("user_id")
    private Long userId;
}
