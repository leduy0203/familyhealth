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
            Bạn là bác sĩ tư vấn chuyên nghiệp với nhiều năm kinh nghiệm. Hãy phân tích kỹ lưỡng tình trạng của bệnh nhân và đưa ra tư vấn chi tiết bằng tiếng Việt.
            
            Thông tin bệnh nhân:
            - Triệu chứng: {symptoms}
            - Tuổi: {age}
            - Giới tính: {gender}
            - Tiền sử bệnh: {medicalHistory}
            
            Danh sách bác sĩ phù hợp:
            {doctorContext}
            
            Yêu cầu:
            1. Viết lời khuyên CHI TIẾT, DỄ HIỂU, KHÔNG dùng markdown (*, **, #, bullets)
            2. Viết thành đoạn văn tự nhiên, như đang tư vấn trực tiếp cho bệnh nhân
            3. Phân tích ít nhất 5-8 câu về:
               - Nguyên nhân có thể gây ra triệu chứng
               - Các biện pháp chăm sóc tại nhà cụ thể
               - Dấu hiệu cảnh báo cần đi khám ngay
               - Lời khuyên về chế độ ăn uống, sinh hoạt
               - Thời gian theo dõi và khi nào cần tái khám
            4. KHÔNG hỏi lại thông tin bệnh nhân
            5. KHÔNG liệt kê danh sách gạch đầu dòng, viết thành đoạn văn
            6. Sử dụng ngôn ngữ thân thiện, dễ hiểu nhưng chuyên nghiệp
            
            Trả về dưới dạng JSON:
            {{
              "advice": "lời khuyên chi tiết 5-8 câu bằng văn xuôi, không có ký tự markdown",
              "severity": "LOW|MEDIUM|HIGH",
              "recommendedAction": "REST|HOME_CARE|SEE_DOCTOR|EMERGENCY",
              "requiresDoctor": true/false,
              "recommendedDoctors": [
                {{"doctorId": số, "fullname": "tên", "expertise": "chuyên khoa", "bio": "tiểu sử", "matchScore": 0.0-1.0}}
              ]
            }}
            
            Ví dụ advice tốt: "Triệu chứng đau đầu và buồn nôn mà bạn đang gặp có thể do nhiều nguyên nhân khác nhau. Các nguyên nhân phổ biến bao gồm căng thẳng stress, thiếu ngủ, hoặc có thể là dấu hiệu của vấn đề tiêu hóa hoặc huyết áp. Với triệu chứng này, tôi khuyên bạn nên nghỉ ngơi trong phòng tối, yên tĩnh, tránh ánh sáng chói và tiếng ồn. Hãy uống nhiều nước lọc, ít nhất 2 lít mỗi ngày, và ăn những bữa ăn nhẹ, dễ tiêu như cháo hoặc súp. Bạn có thể chườm khăn ấm lên trán và massage nhẹ vùng thái dương để giảm đau. Tránh sử dụng điện thoại, máy tính và các thiết bị điện tử trong 2-3 tiếng đầu. Nếu sau 24 giờ tình trạng không thuyên giảm, hoặc nếu xuất hiện các triệu chứng nghiêm trọng hơn như đau đầu dữ dội đột ngột, sốt cao trên 39 độ, buồn nôn kèm lú lẫn hoặc co giật, bạn cần đến bệnh viện ngay lập tức để được kiểm tra kỹ hơn."
            """;
    
    @Override
    public HealthAdviceResponse getHealthAdvice(HealthAdviceRequest request) {
        log.info("Providing health consultation for: {}", request.getSymptoms());
        
        try {
            // Step 1: Vector search for relevant doctors
            // Không dùng similarity threshold để luôn trả về bác sĩ
            SearchRequest searchRequest = SearchRequest.query(request.getSymptoms())
                    .withTopK(5);
            
            List<Document> similarDoctors = vectorStore.similaritySearch(searchRequest);
            log.info("Found {} doctors from vector search for symptoms: {}", similarDoctors.size(), request.getSymptoms());
            
            if (similarDoctors.isEmpty()) {
                log.warn("No doctors found! Vector store might be empty. Checking...");
                // Thử search với query rỗng để kiểm tra vector store
                SearchRequest checkRequest = SearchRequest.query("bác sĩ doctor").withTopK(10);
                List<Document> allDocs = vectorStore.similaritySearch(checkRequest);
                log.info("Total documents in vector store: {}", allDocs.size());
                similarDoctors = allDocs;
            }
            
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
            log.info("Raw JSON response: {}", jsonResponse);
            
            // Extract advice fields
            String advice = extractJsonField(jsonResponse, "advice");
            String severity = extractJsonField(jsonResponse, "severity");
            String recommendedAction = extractJsonField(jsonResponse, "recommendedAction");
            
            // Fix: Better parsing for requiresDoctor boolean
            boolean requiresDoctor = false;
            java.util.regex.Pattern requiresDoctorPattern = java.util.regex.Pattern.compile(
                    "\"requiresDoctor\"\\s*:\\s*(true|false)", java.util.regex.Pattern.CASE_INSENSITIVE);
            java.util.regex.Matcher requiresDoctorMatcher = requiresDoctorPattern.matcher(jsonResponse);
            if (requiresDoctorMatcher.find()) {
                requiresDoctor = "true".equalsIgnoreCase(requiresDoctorMatcher.group(1));
            }
            
            log.info("requiresDoctor from AI: {}, documents size: {}", requiresDoctor, documents.size());
            
            // Extract or create doctor recommendations
            List<HealthAdviceResponse.RecommendedDoctor> recommendedDoctors = new ArrayList<>();
            
            // LUÔN thêm bác sĩ nếu có documents, không chỉ khi requiresDoctor = true
            // Vì AI có thể không trả về đúng requiresDoctor
            if (!documents.isEmpty()) {
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
                // Nếu có bác sĩ phù hợp, set requiresDoctor = true
                requiresDoctor = true;
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
