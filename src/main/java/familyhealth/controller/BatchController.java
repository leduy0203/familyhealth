package familyhealth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/batch")
@RequiredArgsConstructor
@Slf4j
public class BatchController {

    private final JobLauncher asyncJobLauncher;
    private final Job exportUsersJob;
    private final Job exportDoctorsJob;

    @PostMapping("/export/users")
    public ResponseEntity<Map<String, Object>> exportUsers() {
        try {
            log.info("Starting users export batch job");
            
            // Create unique job parameters to allow multiple executions
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            
            JobExecution jobExecution = asyncJobLauncher.run(exportUsersJob, jobParameters);
            
            Map<String, Object> response = new HashMap<>();
            response.put("jobId", jobExecution.getJobId());
            response.put("status", "STARTED");
            response.put("message", "User export job started asynchronously");
            response.put("startTime", jobExecution.getCreateTime());
            
            log.info("Users export job completed with status: {}", jobExecution.getStatus());
            
            return ResponseEntity.ok(response);
            
        } catch (JobExecutionAlreadyRunningException e) {
            log.error("Job is already running", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Job is already running"));
        } catch (JobRestartException e) {
            log.error("Job restart error", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Job restart error"));
        } catch (JobInstanceAlreadyCompleteException e) {
            log.error("Job already completed", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Job already completed"));
        } catch (JobParametersInvalidException e) {
            log.error("Invalid job parameters", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid job parameters"));
        } catch (Exception e) {
            log.error("Error executing users export job", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Error executing export job"));
        }
    }

    @PostMapping("/export/doctors")
    public ResponseEntity<Map<String, Object>> exportDoctors() {
        try {
            log.info("Starting doctors export batch job");
            
            // Create unique job parameters to allow multiple executions
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            
            JobExecution jobExecution = asyncJobLauncher.run(exportDoctorsJob, jobParameters);
            
            Map<String, Object> response = new HashMap<>();
            response.put("jobId", jobExecution.getJobId());
            response.put("status", "STARTED");
            response.put("message", "Doctor export job started asynchronously");
            response.put("startTime", jobExecution.getCreateTime());
            
            log.info("Doctors export job completed with status: {}", jobExecution.getStatus());
            
            return ResponseEntity.ok(response);
            
        } catch (JobExecutionAlreadyRunningException e) {
            log.error("Job is already running", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Job is already running"));
        } catch (JobRestartException e) {
            log.error("Job restart error", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Job restart error"));
        } catch (JobInstanceAlreadyCompleteException e) {
            log.error("Job already completed", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Job already completed"));
        } catch (JobParametersInvalidException e) {
            log.error("Invalid job parameters", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid job parameters"));
        } catch (Exception e) {
            log.error("Error executing doctors export job", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Error executing export job"));
        }
    }

    @GetMapping("/jobs/{jobId}/status")
    public ResponseEntity<Map<String, Object>> getJobStatus(@PathVariable Long jobId) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Job status endpoint - implementation depends on JobExplorer if needed");
        response.put("jobId", jobId);
        return ResponseEntity.ok(response);
    }
}
