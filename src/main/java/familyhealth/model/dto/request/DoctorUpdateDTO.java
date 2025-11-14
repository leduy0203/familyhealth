package familyhealth.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import familyhealth.common.Expertise;
import familyhealth.model.dto.PersonDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorUpdateDTO extends PersonDTO {
    @NotNull(message = "Chuyên ngành không được null")
    @JsonProperty("expertise")
    private Expertise expertise;

    @NotBlank(message = "Bio không được để trống")
    @Size(max = 255, message = "Bio tối đa 255 ký tự")
    @JsonProperty("bio")
    private String bio;
}
