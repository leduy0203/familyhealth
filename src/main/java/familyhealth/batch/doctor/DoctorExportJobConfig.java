package familyhealth.batch.doctor;

import familyhealth.model.Doctor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DoctorExportJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DoctorItemReader doctorItemReader;
    private final DoctorItemWriter doctorItemWriter;

    @Bean
    public Job exportDoctorsJob() {
        return new JobBuilder("exportDoctorsJob", jobRepository)
                .start(exportDoctorsStep())
                .build();
    }

    @Bean
    public Step exportDoctorsStep() {
        return new StepBuilder("exportDoctorsStep", jobRepository)
                .<Doctor, Doctor>chunk(100, transactionManager)
                .reader(doctorItemReader)
                .writer(doctorItemWriter)
                .build();
    }
}
