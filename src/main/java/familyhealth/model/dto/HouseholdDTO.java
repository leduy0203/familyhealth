package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseholdDTO {
    @JsonProperty("househead_id")
    private Long househeadId;

    @NotBlank(message = "Address không được để trống")
    @Size(max = 200, message = "Address tối đa 200 ký tự")
    @JsonProperty("address")
    private String address;

    @NotNull(message = "Quantity không được null")
    @Min(value = 1, message = "Quantity phải >= 1")
    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("is_active")
    private Boolean isActive;
}