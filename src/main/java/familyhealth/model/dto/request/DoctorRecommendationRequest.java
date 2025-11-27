package familyhealth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRecommendationRequest {
    
    @NotBlank(message = "Condition or symptoms is required")
    private String condition;
    
    private String preferredExpertise;
    
    private String location;
}
