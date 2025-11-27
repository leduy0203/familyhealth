package familyhealth.service;

import org.springframework.core.io.Resource;

public interface IReportService {
    byte[] generateMemberReport(Long memberId);
    String saveMemberReportToFile(Long memberId, String outputDirectory);
    Resource getReportFile(String filePath);
}
