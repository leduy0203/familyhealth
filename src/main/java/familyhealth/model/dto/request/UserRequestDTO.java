package familyhealth.model.dto.request;

import familyhealth.common.Gender;
import familyhealth.common.MemberStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequestDTO {

    @NotBlank(message = "Phone is not blank")
    private String phone;

    @NotBlank(message = "Password is not blank")
    private String password;

    @NotBlank(message = "Role is not blank")
    private Long roleId;

    @NotBlank(message = "Full name is not blank")
    private Boolean isActive = true;

    private MemberInfo memberInfo;

    @Getter
    @Setter
    public static class MemberInfo {

        @NotBlank(message = "Full name is not blank")
        private String fullName;

        @NotBlank(message = "Address is not blank")
        private String address;

        private Gender gender;

        private LocalDate dateOfBirth;

        @NotBlank(message = "Bhyt is not blank")
        private String bhyt;
        @NotBlank(message = "CCCD is not blank")
        private String cccd;

    }
}


