package familyhealth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import java.io.Serializable;

@Getter
public class LogoutRequest implements Serializable {

    @NotBlank(message = "Access token cannot be blank")
    private String accessToken;
}
