package familyhealth.service;

import familyhealth.model.dto.request.HealthAdviceRequest;
import familyhealth.model.dto.response.HealthAdviceResponse;

public interface IHealthAdvisorService {
    
    /**
     * Provide health advice and recommend suitable doctors based on symptoms using RAG
     */
    HealthAdviceResponse getHealthAdvice(HealthAdviceRequest request);
}
