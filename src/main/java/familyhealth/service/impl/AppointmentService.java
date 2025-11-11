package familyhealth.service.impl;

import familyhealth.common.AppointmentStatus;
import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.mapper.AppointmentMapper;
import familyhealth.model.Appointment;
import familyhealth.model.Doctor;
import familyhealth.model.Member;
import familyhealth.model.dto.AppointmentDTO;
import familyhealth.repository.AppointmentRepository;
import familyhealth.service.IAppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppointmentService implements IAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final MemberService memberService;

    @Override
    public Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
    }

    @Override
    public Appointment createAppointment(AppointmentDTO appointmentDTO) {
        Doctor doctor = doctorService.getDoctor(appointmentDTO.getDoctorId());
        Member member = memberService.getMember(appointmentDTO.getMemberId());
        Appointment appointment = AppointmentMapper.convertToAppointment(appointmentDTO, doctor, member);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        Appointment appointment = getAppointment(id);
        Appointment updateAppointment = AppointmentMapper.convertToAppointment(appointmentDTO, appointment.getDoctor(), appointment.getMember());
        updateAppointment.setId(appointment.getId());
        return appointmentRepository.save(updateAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = getAppointment(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }
}
