package familyhealth.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import familyhealth.common.Gender;
import familyhealth.common.Relation;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberRegisterDTO {

    @NotNull(message = "Relation không được null")
    @JsonProperty("relation")
    private Relation relation;

    @NotBlank(message = "BHYT không được để trống")
    @Size(max = 12, message = "BHYT tối đa 12 ký tự")
    @JsonProperty("bhyt")
    private String bhyt;

    @JsonProperty("fullname")
    private String fullname;

    @NotBlank(message = "Address không được để trống")
    @Size(max = 200, message = "Address tối đa 200 ký tự")
    @JsonProperty("address")
    private String address;

    @NotNull(message = "Gender không được null")
    @JsonProperty("gender")
    private Gender gender;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate dateOfBirth;

    @Email(message = "Email không hợp lệ")
    @JsonProperty("email")
    private String email;

    @Size(max = 12, message = "CCCD tối đa 12 ký tự")
    private String idCard;


    @JsonProperty("user_account")
    private UserAccount userAccount;
    @Data
    public static class UserAccount {

        @Size(max = 10, message = "Phone tối đa 10 ký tự")
        @JsonProperty("phone")
        private String phone;

        @JsonProperty("password")
        private String password;

    }
}

