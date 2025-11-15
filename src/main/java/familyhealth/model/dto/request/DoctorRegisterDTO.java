package familyhealth.model.dto.request; // Đặt DTO vào một package duy nhất

import com.fasterxml.jackson.annotation.JsonProperty;
import familyhealth.common.Expertise;
import familyhealth.model.dto.PersonDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DoctorRegisterDTO extends PersonDTO {

    @NotBlank(message = "Phone không được để trống")
    @Size(max = 10, message = "Phone tối đa 10 ký tự")
    @JsonProperty("phone")
    private String phone;

    @NotBlank(message = "Password không được để trống")
    @JsonProperty("password")
    private String password;

    @NotNull(message = "Chuyên ngành không được null")
    @JsonProperty("expertise")
    private Expertise expertise;

    @NotBlank(message = "Bio không được để trống")
    @Size(max = 255, message = "Bio tối đa 255 ký tự")
    @JsonProperty("bio")
    private String bio;

    private Boolean isActive;
}