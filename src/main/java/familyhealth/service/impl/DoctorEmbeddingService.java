package familyhealth.service.impl;

import familyhealth.model.Doctor;
import familyhealth.repository.DoctorRepository;
import familyhealth.service.IDoctorEmbeddingService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

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
    
    @PostConstruct
    public void init() {
        log.info("Initializing doctor embeddings...");
        indexAllDoctors();
    }
    
    @Override
    public void indexAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        log.info("Indexing {} doctors", doctors.size());
        
        List<Document> documents = doctors.stream()
                .map(this::createDocument)
                .collect(Collectors.toList());
        
        if (!documents.isEmpty()) {
            vectorStore.add(documents);
            log.info("Successfully indexed {} doctors", documents.size());
        }
    }
    
    @Override
    public void indexDoctor(Doctor doctor) {
        Document document = createDocument(doctor);
        vectorStore.add(List.of(document));
        log.info("Indexed doctor: {}", doctor.getFullname());
    }
    
    @Override
    public void removeDoctor(Long doctorId) {
        vectorStore.delete(List.of(String.valueOf(doctorId)));
        log.info("Removed doctor from index: {}", doctorId);
    }
    
    @Override
    public void updateDoctorIndex(Doctor doctor) {
        removeDoctor(doctor.getId());
        indexDoctor(doctor);
        log.info("Updated doctor index: {}", doctor.getFullname());
    }
    
    private Document createDocument(Doctor doctor) {
        // Create comprehensive text representation of doctor
        StringBuilder content = new StringBuilder();
        content.append("Doctor: ").append(doctor.getFullname()).append("\n");
        content.append("Expertise: ").append(doctor.getExpertise()).append("\n");
        content.append("Bio: ").append(doctor.getBio()).append("\n");
        content.append("Gender: ").append(doctor.getGender()).append("\n");
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
}
