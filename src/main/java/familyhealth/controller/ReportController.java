package familyhealth.controller;

import familyhealth.service.IReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Report", description = "Report API")
public class ReportController {
    
    private final IReportService reportService;
    
    @Operation(summary = "Export member medical record to PDF (download)")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<byte[]> exportMemberReport(@PathVariable Long memberId) {
        log.info("Exporting report for member: {}", memberId);
        
        byte[] pdfBytes = reportService.generateMemberReport(memberId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "member_report_" + memberId + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
    
    @Operation(summary = "Save member medical record to file on server")
    @PostMapping("/member/{memberId}/save")
    public ResponseEntity<Map<String, String>> saveMemberReportToFile(
            @PathVariable Long memberId,
            @Parameter(description = "Output directory path (optional, uses default if not provided)")
            @RequestParam(required = false) String outputDirectory) {
        
        log.info("Saving report to file for member: {}", memberId);
        
        String filePath = reportService.saveMemberReportToFile(memberId, outputDirectory);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Report saved successfully");
        response.put("filePath", filePath);
        response.put("memberId", memberId.toString());
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "View/download saved report file by path")
    @GetMapping("/file")
    public ResponseEntity<Resource> getReportFile(
            @Parameter(description = "Full file path returned from save API")
            @RequestParam String filePath) {
        
        log.info("Retrieving report file: {}", filePath);
        
        Resource resource = reportService.getReportFile(filePath);
        
        String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", filename);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
