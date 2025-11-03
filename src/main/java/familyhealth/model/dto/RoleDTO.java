<<<<<<< HEAD:src/main/java/familyhealth/model/dto/RoleDTO.java
package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @NotBlank(message = "Tên role không được để trống")
    @JsonProperty("name")
    private String name;
=======
package vn.familyhealth.model.dto;

public class RoleDTO {
>>>>>>> f3ef2a1816e0a6ceed82d37f450c23fd4ce4c822:src/main/java/vn/familyhealth/model/dto/RoleDTO.java
}
