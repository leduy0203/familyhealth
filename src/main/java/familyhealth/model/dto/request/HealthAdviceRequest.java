package familyhealth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthAdviceRequest {
    
    @NotBlank(message = "Symptoms or question is required")
    private String symptoms;
    
    private String medicalHistory;
    
    private Integer age;
    
    private String gender;
}
