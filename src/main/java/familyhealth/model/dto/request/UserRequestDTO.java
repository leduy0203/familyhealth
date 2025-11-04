package familyhealth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import familyhealth.common.Gender;

import java.time.LocalDateTime;

@Getter
public class UserRequestDTO {

    @NotBlank(message = "Email is not blank")
    private String email;

    @NotBlank(message = "Password is not blank")
    private String password;

    private String phone;

    @NotBlank(message = "Full name is not blank")
    private String fullName;

    @NotBlank(message = "Address is not blank")
    private String address;

    private Gender gender;

    private LocalDateTime dateOfBirth;

    private String getCccd;
}
