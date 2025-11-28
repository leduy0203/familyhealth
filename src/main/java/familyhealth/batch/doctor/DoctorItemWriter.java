package familyhealth.batch.doctor;

import familyhealth.model.Doctor;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class DoctorItemWriter implements ItemWriter<Doctor> {

    @Value("${batch.export.directory:d:/exports}")
    private String exportDirectory;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FILE_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @Override
    public void write(Chunk<? extends Doctor> chunk) throws Exception {
        // Create directory if not exists
        Path dirPath = Paths.get(exportDirectory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // Generate filename with timestamp
        String timestamp = LocalDateTime.now().format(FILE_DATETIME_FORMATTER);
        String filename = String.format("doctors_export_%s.xlsx", timestamp);
        Path filePath = dirPath.resolve(filename);

        // Create Excel workbook
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {

            Sheet sheet = workbook.createSheet("Doctors");

            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Họ tên", "CCCD", "Giới tính", "Ngày sinh", "Email", 
                               "Địa chỉ", "Chuyên môn", "Mô tả", "Số điện thoại"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            int rowNum = 1;
            for (Doctor doctor : chunk.getItems()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(doctor.getId());
                row.createCell(1).setCellValue(doctor.getFullname() != null ? doctor.getFullname() : "N/A");
                row.createCell(2).setCellValue(doctor.getIdCard() != null ? doctor.getIdCard() : "N/A");
                row.createCell(3).setCellValue(doctor.getGender() != null ? doctor.getGender().name() : "N/A");
                row.createCell(4).setCellValue(doctor.getDateOfBirth() != null ? 
                    doctor.getDateOfBirth().format(DATE_FORMATTER) : "N/A");
                row.createCell(5).setCellValue(doctor.getEmail() != null ? doctor.getEmail() : "N/A");
                row.createCell(6).setCellValue(doctor.getAddress() != null ? doctor.getAddress() : "N/A");
                row.createCell(7).setCellValue(doctor.getExpertise() != null ? doctor.getExpertise().name() : "N/A");
                row.createCell(8).setCellValue(doctor.getBio() != null ? doctor.getBio() : "");
                row.createCell(9).setCellValue(doctor.getUser() != null ? doctor.getUser().getPhone() : "N/A");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(fileOut);
        } catch (IOException e) {
            throw new RuntimeException("Error writing doctors to Excel file", e);
        }
    }
}
