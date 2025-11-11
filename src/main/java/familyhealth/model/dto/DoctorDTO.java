package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import familyhealth.common.Expertise;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO extends PersonDTO{
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
