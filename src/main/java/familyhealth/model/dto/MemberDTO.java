package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;
import familyhealth.common.Relation;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO extends PersonDTO{
    @NotNull(message = "Relation không được null")
    @JsonProperty("relation")
    private Relation relation;

    @NotBlank(message = "BHYT không được để trống")
    @Size(max = 12, message = "BHYT tối đa 12 ký tự")
    @JsonProperty("bhyt")
    private String bhyt;

    @NotNull(message = "Household ID không được null")
    @JsonProperty("household_id")
    private Long householdId;

    @JsonProperty("user_id")
    private Long userId;
}
