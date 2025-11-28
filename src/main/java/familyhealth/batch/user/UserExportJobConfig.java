package familyhealth.batch.user;

import familyhealth.model.User;
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
public class UserExportJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final UserItemReader userItemReader;
    private final UserItemWriter userItemWriter;

    @Bean
    public Job exportUsersJob() {
        return new JobBuilder("exportUsersJob", jobRepository)
                .start(exportUsersStep())
                .build();
    }

    @Bean
    public Step exportUsersStep() {
        return new StepBuilder("exportUsersStep", jobRepository)
                .<User, User>chunk(100, transactionManager)
                .reader(userItemReader)
                .writer(userItemWriter)
                .build();
    }
}
