package familyhealth.service;

import familyhealth.model.MedicalResult;

public interface IEmailService {
    
    /**
     * Gửi email thông báo kết quả khám bệnh cho bệnh nhân
     * @param medicalResult kết quả khám bệnh
     */
    void sendMedicalResultEmail(MedicalResult medicalResult);
    
    /**
     * Gửi email đơn giản
     * @param to địa chỉ email nhận
     * @param subject tiêu đề email
     * @param content nội dung email
     */
    void sendSimpleEmail(String to, String subject, String content);
    
    /**
     * Gửi email với template HTML
     * @param to địa chỉ email nhận
     * @param subject tiêu đề email
     * @param templateName tên template
     * @param variables các biến truyền vào template
     */
    void sendHtmlEmail(String to, String subject, String templateName, java.util.Map<String, Object> variables);
}
