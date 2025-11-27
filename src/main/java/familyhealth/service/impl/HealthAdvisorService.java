package familyhealth.service.impl;

import familyhealth.model.dto.request.HealthAdviceRequest;
import familyhealth.model.dto.response.HealthAdviceResponse;
import familyhealth.service.IHealthAdvisorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthAdvisorService implements IHealthAdvisorService {
    
    private final ChatClient.Builder chatClientBuilder;
    private final VectorStore vectorStore;
    
    private static final String COMBINED_CONSULTATION_TEMPLATE = """
            Bạn là bác sĩ tư vấn chuyên nghiệp. Hãy phân tích tình trạng của bệnh nhân và đưa ra tư vấn bằng tiếng Việt.
            
            Thông tin bệnh nhân:
            - Triệu chứng: {symptoms}
            - Tuổi: {age}
            - Giới tính: {gender}
            - Tiền sử bệnh: {medicalHistory}
            
            Danh sách bác sĩ phù hợp:
            {doctorContext}
            
            Yêu cầu:
            1. Viết lời khuyên ngắn gọn, dễ hiểu, KHÔNG dùng markdown (*, **, #, bullets)
            2. Viết thành đoạn văn tự nhiên, như đang nói chuyện trực tiếp
            3. Tối đa 3-4 câu ngắn gọn về triệu chứng và cách xử lý
            4. KHÔNG hỏi lại thông tin bệnh nhân
            5. KHÔNG liệt kê danh sách gạch đầu dòng
            
            Trả về dưới dạng JSON:
            {{
              "advice": "lời khuyên ngắn gọn bằng văn xuôi, không có ký tự markdown",
              "severity": "LOW|MEDIUM|HIGH",
              "recommendedAction": "REST|HOME_CARE|SEE_DOCTOR|EMERGENCY",
              "requiresDoctor": true/false,
              "recommendedDoctors": [
                {{"doctorId": số, "fullname": "tên", "expertise": "chuyên khoa", "bio": "tiểu sử", "matchScore": 0.0-1.0}}
              ]
            }}
            
            Ví dụ advice tốt: "Triệu chứng đau đầu và buồn nôn có thể do nhiều nguyên nhân như căng thẳng, mất ngủ hoặc vấn đề tiêu hóa. Bạn nên nghỉ ngơi trong phòng tối, uống nhiều nước và tránh ánh sáng chói. Nếu tình trạng không cải thiện sau 24 giờ hoặc có thêm triệu chứng như sốt cao, đau dữ dội, hãy đến bệnh viện ngay."
            """;
    
    @Override
    public HealthAdviceResponse getHealthAdvice(HealthAdviceRequest request) {
        log.info("Providing health consultation for: {}", request.getSymptoms());
        
        try {
            // Step 1: Vector search for relevant doctors (lower threshold for better matching)
            SearchRequest searchRequest = SearchRequest.query(request.getSymptoms())
                    .withTopK(5)
                    .withSimilarityThreshold(0.3); // Giảm từ 0.5 xuống 0.3 để dễ match hơn
            
            List<Document> similarDoctors = vectorStore.similaritySearch(searchRequest);
            log.info("Found {} doctors from vector search", similarDoctors.size());
            similarDoctors.forEach(doc -> log.info("Doctor: {}, Expertise: {}", 
                    doc.getMetadata().get("fullname"), doc.getMetadata().get("expertise")));
            
            // Step 2: Create doctor context
            String doctorContext = similarDoctors.isEmpty() ? 
                    "No specific doctors available" :
                    similarDoctors.stream()
                            .map(doc -> String.format(
                                    "ID: %s, Name: %s, Expertise: %s, Bio: %s",
                                    doc.getMetadata().get("doctorId"),
                                    doc.getMetadata().get("fullname"),
                                    doc.getMetadata().get("expertise"),
                                    doc.getContent()
                            ))
                            .collect(Collectors.joining("\n"));
            
            // Step 3: Generate combined consultation
            PromptTemplate promptTemplate = new PromptTemplate(COMBINED_CONSULTATION_TEMPLATE);
            
            Map<String, Object> params = Map.of(
                    "symptoms", request.getSymptoms(),
                    "age", request.getAge() != null ? request.getAge().toString() : "Not specified",
                    "gender", request.getGender() != null ? request.getGender() : "Not specified",
                    "medicalHistory", request.getMedicalHistory() != null ? request.getMedicalHistory() : "None",
                    "doctorContext", doctorContext
            );
            
            Prompt prompt = promptTemplate.create(params);
            
            String response = chatClientBuilder.build()
                    .prompt(prompt)
                    .call()
                    .content();
            
            log.info("AI Consultation Response: {}", response);
            
            return parseConsultationResponse(response, similarDoctors);
            
        } catch (Exception e) {
            log.error("Error in health consultation", e);
            return HealthAdviceResponse.builder()
                    .advice("Unable to provide consultation at this time. Please consult a healthcare professional.")
                    .severity("MEDIUM")
                    .recommendedAction("SEE_DOCTOR")
                    .requiresDoctor(true)
                    .recommendedDoctors(List.of())
                    .build();
        }
    }
    
    private HealthAdviceResponse parseConsultationResponse(String jsonResponse, List<Document> documents) {
        try {
            // Remove markdown code blocks if present
            jsonResponse = jsonResponse.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();
            
            log.info("Parsing JSON response for doctors");
            
            // Extract advice fields
            String advice = extractJsonField(jsonResponse, "advice");
            String severity = extractJsonField(jsonResponse, "severity");
            String recommendedAction = extractJsonField(jsonResponse, "recommendedAction");
            
            // Fix: Check for both "true" string and boolean pattern
            boolean requiresDoctor = jsonResponse.contains("\"requiresDoctor\"") && 
                    (jsonResponse.contains(": true") || jsonResponse.contains(":true") || 
                     jsonResponse.contains("\"requiresDoctor\": true") || jsonResponse.contains("\"requiresDoctor\":true"));
            
            log.info("requiresDoctor from AI: {}, documents size: {}", requiresDoctor, documents.size());
            
            // Extract or create doctor recommendations
            List<HealthAdviceResponse.RecommendedDoctor> recommendedDoctors = new ArrayList<>();
            
            if (requiresDoctor && !documents.isEmpty()) {
                log.info("Creating doctor recommendations from {} documents", documents.size());
                recommendedDoctors = documents.stream()
                        .limit(3)
                        .map(doc -> {
                            log.info("Adding doctor: {}", doc.getMetadata().get("fullname"));
                            return HealthAdviceResponse.RecommendedDoctor.builder()
                                    .doctorId(Long.parseLong(doc.getMetadata().get("doctorId").toString()))
                                    .fullname(doc.getMetadata().get("fullname").toString())
                                    .expertise(doc.getMetadata().get("expertise").toString())
                                    .bio(doc.getContent().length() > 150 ? 
                                         doc.getContent().substring(0, 150) + "..." : 
                                         doc.getContent())
                                    .matchScore(0.88)
                                    .build();
                        })
                        .collect(Collectors.toList());
            }
            
            log.info("Final recommendedDoctors count: {}", recommendedDoctors.size());
            
            return HealthAdviceResponse.builder()
                    .advice(advice != null ? advice : "Please consult a healthcare professional for proper diagnosis.")
                    .severity(severity != null ? severity : "MEDIUM")
                    .recommendedAction(recommendedAction != null ? recommendedAction : "SEE_DOCTOR")
                    .requiresDoctor(requiresDoctor)
                    .recommendedDoctors(recommendedDoctors)
                    .build();
                    
        } catch (Exception e) {
            log.error("Error parsing consultation response", e);
            return HealthAdviceResponse.builder()
                    .advice("Please consult a healthcare professional.")
                    .severity("MEDIUM")
                    .recommendedAction("SEE_DOCTOR")
                    .requiresDoctor(true)
                    .recommendedDoctors(List.of())
                    .build();
        }
    }
    
    private String extractJsonField(String json, String fieldName) {
        try {
            String pattern = "\"" + fieldName + "\"\\s*:\\s*\"([^\"]+)\"";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(json);
            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception e) {
            log.warn("Could not extract field: {}", fieldName);
        }
        return null;
    }
}
