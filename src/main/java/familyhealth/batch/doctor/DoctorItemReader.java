package familyhealth.batch.doctor;

import familyhealth.model.Doctor;
import familyhealth.repository.DoctorRepository;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DoctorItemReader extends RepositoryItemReader<Doctor> {

    public DoctorItemReader(DoctorRepository doctorRepository) {
        setRepository(doctorRepository);
        setMethodName("findAll");
        
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        setSort(sorts);
        
        setPageSize(100);
    }
}
