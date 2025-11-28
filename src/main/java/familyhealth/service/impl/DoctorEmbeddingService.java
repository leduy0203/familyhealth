package familyhealth.service.impl;

import familyhealth.common.Expertise;
import familyhealth.model.Doctor;
import familyhealth.repository.DoctorRepository;
import familyhealth.service.IDoctorEmbeddingService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorEmbeddingService implements IDoctorEmbeddingService {
    
    private final VectorStore vectorStore;
    private final DoctorRepository doctorRepository;
    
    private static final String VECTOR_STORE_FILE = "vector-store.json";
    
    @PostConstruct
    public void init() {
        log.info("Initializing doctor embeddings...");
        indexAllDoctors();
    }
    
    @Override
    public void indexAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        log.info("Found {} doctors in database", doctors.size());
        
        if (doctors.isEmpty()) {
            log.warn("No doctors found in database! Please add doctors first.");
            return;
        }
        
        List<Document> documents = doctors.stream()
                .map(this::createDocument)
                .collect(Collectors.toList());
        
        if (!documents.isEmpty()) {
            vectorStore.add(documents);
            log.info("Successfully indexed {} doctors", documents.size());
            
            // Lưu vector store ngay sau khi index
            saveVectorStore();
        }
    }
    
    private void saveVectorStore() {
        try {
            if (vectorStore instanceof SimpleVectorStore simpleVectorStore) {
                File vectorStoreFile = Paths.get(VECTOR_STORE_FILE).toFile();
                simpleVectorStore.save(vectorStoreFile);
                log.info("Saved vector store to file after indexing");
            }
        } catch (Exception e) {
            log.error("Could not save vector store: {}", e.getMessage());
        }
    }
    
    @Override
    public void indexDoctor(Doctor doctor) {
        Document document = createDocument(doctor);
        vectorStore.add(List.of(document));
        log.info("Indexed doctor: {}", doctor.getFullname());
        saveVectorStore();
    }
    
    @Override
    public void removeDoctor(Long doctorId) {
        vectorStore.delete(List.of(String.valueOf(doctorId)));
        log.info("Removed doctor from index: {}", doctorId);
        saveVectorStore();
    }
    
    @Override
    public void updateDoctorIndex(Doctor doctor) {
        removeDoctor(doctor.getId());
        indexDoctor(doctor);
        log.info("Updated doctor index: {}", doctor.getFullname());
    }
    
    private Document createDocument(Doctor doctor) {
        // Create comprehensive text representation of doctor with Vietnamese keywords
        StringBuilder content = new StringBuilder();
        content.append("Bác sĩ: ").append(doctor.getFullname()).append("\n");
        content.append("Chuyên khoa: ").append(doctor.getExpertise()).append(" ");
        content.append(getExpertiseKeywords(doctor.getExpertise())).append("\n");
        content.append("Giới thiệu: ").append(doctor.getBio()).append("\n");
        content.append("Giới tính: ").append(doctor.getGender()).append("\n");
        content.append("Email: ").append(doctor.getEmail()).append("\n");
        
        // Metadata for filtering and retrieval
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("doctorId", doctor.getId());
        metadata.put("fullname", doctor.getFullname());
        metadata.put("expertise", doctor.getExpertise().toString());
        metadata.put("email", doctor.getEmail());
        metadata.put("type", "doctor");
        
        return new Document(
                String.valueOf(doctor.getId()),
                content.toString(),
                metadata
        );
    }
    
    /**
     * Trả về các từ khóa triệu chứng liên quan đến chuyên khoa để cải thiện vector search
     */
    private String getExpertiseKeywords(Expertise expertise) {
        if (expertise == null) return "";
        
        return switch (expertise) {
            case TIM_MACH -> "tim mạch đau ngực khó thở huyết áp cao huyết áp thấp nhồi máu cơ tim suy tim rối loạn nhịp tim đau tim tức ngực hồi hộp";
            case HO_HAP -> "hô hấp ho hen suyễn viêm phổi viêm phế quản khó thở đau họng viêm họng cảm cúm sốt ho có đờm ho khan viêm mũi nghẹt mũi";
            case TIEU_HOA -> "tiêu hóa đau bụng đau dạ dày viêm dạ dày trào ngược tiêu chảy táo bón buồn nôn nôn ói đầy bụng khó tiêu viêm ruột đau bụng dưới";
            case THAN_KINH -> "thần kinh đau đầu chóng mặt mất ngủ stress căng thẳng trầm cảm lo âu run tay tê chân đau nửa đầu migraine đột quỵ";
            case TAI_MUI_HONG -> "tai mũi họng viêm tai viêm mũi viêm họng viêm xoang đau họng ù tai chảy máu cam nghẹt mũi viêm amidan khàn tiếng";
            case MAT -> "mắt đau mắt đỏ mắt mờ mắt cận thị viễn thị loạn thị đục thủy tinh thể khô mắt mỏi mắt chảy nước mắt ngứa mắt";
            case DA_LIEU -> "da liễu mụn mẩn ngứa nổi mề đay dị ứng da viêm da chàm vẩy nến nấm da zona rụng tóc";
            case PHU_KHOA -> "phụ khoa kinh nguyệt đau bụng kinh rối loạn kinh nguyệt viêm nhiễm phụ khoa khí hư thai sản mang thai";
        };
    }
}
