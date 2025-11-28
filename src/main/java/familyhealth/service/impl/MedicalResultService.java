package familyhealth.service.impl;

import familyhealth.common.AppointmentStatus;
import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.mapper.MedicalResultMapper;
import familyhealth.model.Appointment;
import familyhealth.model.MedicalResult;
import familyhealth.model.dto.MedicalResultDTO;
import familyhealth.repository.MedicalResultRepository;
import familyhealth.service.IEmailService;
import familyhealth.service.IMedicalResultService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MedicalResultService implements IMedicalResultService {
    private final MedicalResultRepository medicalResultRepository;
    private final AppointmentService appointmentService;
    private final IEmailService emailService;

    @Override
    public MedicalResult getMedicalResult(Long id) {
        return medicalResultRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.MEDICAL_RESULT_NOT_EXISTED));
    }

    @Override
    @Transactional
    public MedicalResult createMedicalResult(MedicalResultDTO medicalResultDTO) {

        Appointment appointment = appointmentService.getAppointment(medicalResultDTO.getAppointmentId());

        appointment.setStatus(AppointmentStatus.COMPLETED);

        MedicalResult medicalResult = MedicalResultMapper.convertToMedicalResult(medicalResultDTO,appointment);

        MedicalResult savedResult = medicalResultRepository.save(medicalResult);
        
        // Gửi email thông báo kết quả khám bệnh (async)
        log.info("Sending medical result email to member: {}", appointment.getMember().getFullname());
        emailService.sendMedicalResultEmail(savedResult);

        return savedResult;
    }

    @Override
    public MedicalResult updateMedicalResult(Long id, MedicalResultDTO medicalResultDTO) {
        MedicalResult medicalResultexisting = getMedicalResult(id);
        MedicalResult medicalResult = MedicalResultMapper.convertToMedicalResult(medicalResultDTO, medicalResultexisting.getAppointment());
        return medicalResultRepository.save(medicalResult);
    }

    @Override
    public void deleteMedicalResult(Long id) {
        MedicalResult medicalResult = getMedicalResult(id);
        medicalResultRepository.delete(medicalResult);
    }
}
