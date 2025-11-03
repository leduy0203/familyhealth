<<<<<<< HEAD:src/main/java/familyhealth/model/dto/TokenDTO.java
package familyhealth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    @NotBlank(message = "Token không được để trống")
    @JsonProperty("token")
    private String token;

    @NotBlank(message = "Token type không được để trống")
    @JsonProperty("token_type")
    private String tokenType;

    @NotNull(message = "Expiration date không được null")
    @JsonProperty("expiration_date")
    private LocalDateTime expirationDate;

    @JsonProperty("revoked")
    private boolean revoked;

    @JsonProperty("expired")
    private boolean expired;

    @NotNull(message = "User ID không được null")
    @JsonProperty("user_id")
    private Long userId;
=======
package vn.familyhealth.model.dto;

public class TokenDTO {
>>>>>>> f3ef2a1816e0a6ceed82d37f450c23fd4ce4c822:src/main/java/vn/familyhealth/model/dto/TokenDTO.java
}
