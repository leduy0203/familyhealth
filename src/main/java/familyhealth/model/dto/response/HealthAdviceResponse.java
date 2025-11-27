package familyhealth.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class HealthAdviceResponse {
    
    private String advice;
    
    private String severity;
    
    private String recommendedAction;
    
    private Boolean requiresDoctor;
    
    private List<RecommendedDoctor> recommendedDoctors;
    
    @Getter
    @Setter
    @Builder
    public static class RecommendedDoctor {
        private Long doctorId;
        private String fullname;
        private String expertise;
        private String bio;
        private Double matchScore;
    }
}
