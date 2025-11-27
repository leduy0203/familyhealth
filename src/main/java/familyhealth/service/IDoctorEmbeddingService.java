package familyhealth.service;

import familyhealth.model.Doctor;

public interface IDoctorEmbeddingService {
    
    /**
     * Index all doctors into vector store
     */
    void indexAllDoctors();
    
    /**
     * Index a single doctor
     */
    void indexDoctor(Doctor doctor);
    
    /**
     * Remove doctor from vector store
     */
    void removeDoctor(Long doctorId);
    
    /**
     * Re-index doctor when updated
     */
    void updateDoctorIndex(Doctor doctor);
}
