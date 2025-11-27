package familyhealth.controller;

import familyhealth.model.dto.request.HealthAdviceRequest;
import familyhealth.model.dto.response.ApiResponse;
import familyhealth.model.dto.response.HealthAdviceResponse;
import familyhealth.service.IHealthAdvisorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "AI Health Advisory", description = "AI-powered health advice and doctor recommendations")
public class AIController {
    
    private final IHealthAdvisorService healthAdvisorService;
    
    @PostMapping("/consult")
    @Operation(summary = "Health consultation with doctor recommendations", 
               description = "Get AI-powered health advice and recommended doctors based on symptoms using RAG")
    public ApiResponse<HealthAdviceResponse> healthConsultation(@RequestBody @Valid HealthAdviceRequest request) {
        log.info("Health consultation request: {}", request.getSymptoms());
        
        HealthAdviceResponse response = healthAdvisorService.getHealthAdvice(request);
        
        return ApiResponse.<HealthAdviceResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Health consultation completed successfully")
                .data(response)
                .build();
    }
}
