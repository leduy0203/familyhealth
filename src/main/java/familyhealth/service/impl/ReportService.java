package familyhealth.service.impl;

import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.model.dto.response.MemberResponse;
import familyhealth.service.IMemberService;
import familyhealth.service.IReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService implements IReportService {
    
    private final IMemberService memberService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FILE_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    @Value("${report.output.directory:reports}")
    private String reportOutputDirectory;

    @Override
    public byte[] generateMemberReport(Long memberId) {
        try {
            log.info("Starting report generation for member: {}", memberId);
            // Get member data
            MemberResponse member = memberService.getMemberDetail(memberId);
            log.info("Retrieved member data - Name: {}, ID: {}, Email: {}", 
                member.getFullname(), member.getIdCard(), member.getEmail());
            log.info("Member medical results count: {}", 
                member.getMedicalResults() != null ? member.getMedicalResults().size() : 0);
            
            // Prepare parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("memberName", member.getFullname());
            parameters.put("idCard", member.getIdCard() != null ? member.getIdCard() : "N/A");
            parameters.put("gender", translateGender(member.getGender()));
            parameters.put("dateOfBirth", member.getDateOfBirth() != null ? 
                member.getDateOfBirth().format(DATE_FORMATTER) : "N/A");
            parameters.put("email", member.getEmail() != null ? member.getEmail() : "N/A");
            parameters.put("bhyt", member.getBhyt() != null ? member.getBhyt() : "Chưa có");
            
            log.info("Parameters prepared: {}", parameters);
            
            // Prepare medical results data
            List<Map<String, Object>> medicalResultsData = member.getMedicalResults().stream()
                .map(result -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", result.getId());
                    row.put("name", result.getName());
                    row.put("diagnose", result.getDiagnose() != null ? result.getDiagnose() : "");
                    row.put("note", result.getNote() != null ? result.getNote() : "");
                    row.put("totalMoney", result.getTotalMoney());
                    row.put("appointmentTime", result.getAppointmentTime() != null ? 
                        result.getAppointmentTime().format(DATETIME_FORMATTER) : "N/A");
                    row.put("doctorName", result.getDoctorName() != null ? result.getDoctorName() : "N/A");
                    return row;
                })
                .collect(Collectors.toList());
            
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(medicalResultsData);
            log.info("Created data source with {} medical results", medicalResultsData.size());
            
            // Load and compile report template
            ClassPathResource resource = new ClassPathResource("reports/member_profile.jrxml");
            log.info("Loading template from: {}", resource.getPath());
            JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
            log.info("Template compiled successfully");
            
            // Fill report
            log.info("Filling report with data...");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            log.info("Report filled successfully. Pages: {}", jasperPrint.getPages().size());
            
            // Export to PDF
            log.info("Exporting to PDF...");
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            log.info("PDF generated successfully, size: {} bytes", pdfBytes.length);
            return pdfBytes;
            
        } catch (Exception e) {
            log.error("Error generating report for member {}: {}", memberId, e.getMessage(), e);
            log.error("Exception type: {}", e.getClass().getName());
            if (e.getCause() != null) {
                log.error("Root cause: {}", e.getCause().getMessage(), e.getCause());
            }
            throw new AppException(ErrorCode.REPORT_GENERATION_ERROR);
        }
    }
    
    private String translateGender(String gender) {
        if (gender == null) return "N/A";
        switch (gender.toUpperCase()) {
            case "MALE":
                return "Nam";
            case "FEMALE":
                return "Nữ";
            case "OTHER":
                return "Khác";
            default:
                return gender;
        }
    }
    
    @Override
    public String saveMemberReportToFile(Long memberId, String outputDirectory) {
        try {
            log.info("Saving report to file for member: {}", memberId);
            
            // Generate PDF bytes
            byte[] pdfBytes = generateMemberReport(memberId);
            
            // Use provided directory or default from config
            String targetDirectory = (outputDirectory != null && !outputDirectory.isEmpty()) 
                ? outputDirectory 
                : reportOutputDirectory;
            
            // Create directory if not exists
            Path dirPath = Paths.get(targetDirectory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                log.info("Created directory: {}", dirPath.toAbsolutePath());
            }
            
            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(FILE_DATETIME_FORMATTER);
            String filename = String.format("member_report_%d_%s.pdf", memberId, timestamp);
            Path filePath = dirPath.resolve(filename);
            
            // Write PDF to file
            Files.write(filePath, pdfBytes);
            
            String absolutePath = filePath.toAbsolutePath().toString();
            log.info("Report saved successfully to: {}", absolutePath);
            
            return absolutePath;
            
        } catch (AppException ae) {
            // Re-throw AppException from generateMemberReport
            throw ae;
        } catch (Exception e) {
            log.error("Error saving report to file for member {}: {}", memberId, e.getMessage(), e);
            log.error("Exception type: {}", e.getClass().getName());
            if (e.getCause() != null) {
                log.error("Root cause: {}", e.getCause().getMessage(), e.getCause());
            }
            throw new AppException(ErrorCode.REPORT_GENERATION_ERROR);
        }
    }
    
    @Override
    public Resource getReportFile(String filePath) {
        try {
            Path file = Paths.get(filePath);
            Resource resource = new UrlResource(file.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                log.info("Retrieved report file: {}", filePath);
                return resource;
            } else {
                log.error("File not found or not readable: {}", filePath);
                throw new AppException(ErrorCode.REPORT_NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            log.error("Invalid file path: {}", filePath, e);
            throw new AppException(ErrorCode.REPORT_NOT_FOUND);
        }
    }
}
