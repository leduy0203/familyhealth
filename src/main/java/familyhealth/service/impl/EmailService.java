package familyhealth.service.impl;

import familyhealth.model.Appointment;
import familyhealth.model.Doctor;
import familyhealth.model.MedicalResult;
import familyhealth.model.Member;
import familyhealth.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendMedicalResultEmail(MedicalResult medicalResult) {
        try {
            Appointment appointment = medicalResult.getAppointment();
            Member member = appointment.getMember();
            Doctor doctor = appointment.getDoctor();

            String memberEmail = member.getEmail();
            
            if (memberEmail == null || memberEmail.isEmpty()) {
                log.warn("Member {} does not have email address, skipping email notification", member.getFullname());
                return;
            }

            // Tạo các biến cho template
            Map<String, Object> variables = new HashMap<>();
            variables.put("memberName", member.getFullname());
            variables.put("doctorName", doctor.getFullname());
            variables.put("doctorExpertise", getExpertiseVietnamese(doctor.getExpertise().toString()));
            variables.put("appointmentDate", appointment.getTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            variables.put("resultName", medicalResult.getName());
            variables.put("diagnose", medicalResult.getDiagnose());
            variables.put("note", medicalResult.getNote());
            variables.put("totalMoney", formatCurrency(medicalResult.getTotalMoney()));
            variables.put("createdAt", medicalResult.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            String subject = "Kết quả khám bệnh - " + medicalResult.getName();
            
            sendHtmlEmail(memberEmail, subject, "medical-result-email", variables);
            
            log.info("Successfully sent medical result email to: {}", memberEmail);
            
        } catch (Exception e) {
            log.error("Failed to send medical result email: {}", e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendSimpleEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            mailSender.send(message);
            log.info("Simple email sent to: {}", to);
            
        } catch (Exception e) {
            log.error("Failed to send simple email to {}: {}", to, e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            // Xử lý template
            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(templateName, context);

            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("HTML email sent to: {}", to);

        } catch (MessagingException e) {
            log.error("Failed to send HTML email to {}: {}", to, e.getMessage(), e);
        }
    }

    private String formatCurrency(Float amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(amount);
    }

    private String getExpertiseVietnamese(String expertise) {
        return switch (expertise) {
            case "TIM_MACH" -> "Tim mạch";
            case "HO_HAP" -> "Hô hấp";
            case "TIEU_HOA" -> "Tiêu hóa";
            case "THAN_KINH" -> "Thần kinh";
            case "TAI_MUI_HONG" -> "Tai mũi họng";
            case "MAT" -> "Mắt";
            case "DA_LIEU" -> "Da liễu";
            case "PHU_KHOA" -> "Phụ khoa";
            default -> expertise;
        };
    }
}
