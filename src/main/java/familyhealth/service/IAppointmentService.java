package familyhealth.service;

import familyhealth.model.Appointment;
import familyhealth.model.dto.AppointmentDTO;
import familyhealth.model.dto.response.AppointmentResponse;
import familyhealth.model.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAppointmentService {
    PageResponse<List<AppointmentResponse>> getAllAppointmentService(String[] search, Pageable pageable);

    Appointment getAppointment(Long id);
    Appointment createAppointment(AppointmentDTO appointmentDTO);
    Appointment updateAppointment(Long id, AppointmentDTO appointmentDTO);
    void deleteAppointment(Long id);
}
