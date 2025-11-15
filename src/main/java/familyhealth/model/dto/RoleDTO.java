package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import familyhealth.common.UserType;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @NotBlank(message = "Tên role không được để trống")
    @JsonProperty("name")
    private UserType name;

    @JsonProperty("is_active")
    private Boolean isActive;
}