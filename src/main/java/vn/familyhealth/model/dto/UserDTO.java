package vn.familyhealth.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {

    @NotBlank(message = "Username is not blank")
    private String username;

    @NotBlank(message = "Password is not blank")
    private String password;
}
