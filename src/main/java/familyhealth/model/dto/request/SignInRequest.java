package familyhealth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import java.io.Serializable;

@Getter
public class SignInRequest implements Serializable {

    @NotBlank(message = "Phone cannot be blank")
    private String phone;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
