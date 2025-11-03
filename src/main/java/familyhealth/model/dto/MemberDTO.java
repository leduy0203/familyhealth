<<<<<<< HEAD:src/main/java/familyhealth/model/dto/MemberDTO.java
package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;
import familyhealth.common.Relation;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
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

    @NotNull(message = "User ID không được null")
    @JsonProperty("user_id")
    private Long userId;
=======
package vn.familyhealth.model.dto;

public class MemberDTO {
>>>>>>> f3ef2a1816e0a6ceed82d37f450c23fd4ce4c822:src/main/java/vn/familyhealth/model/dto/MemberDTO.java
}
