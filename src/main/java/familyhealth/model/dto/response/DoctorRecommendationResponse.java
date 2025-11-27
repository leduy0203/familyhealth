package familyhealth.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DoctorRecommendationResponse {
    
    private List<RecommendedDoctor> doctors;
    
    private String reason;
    
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
