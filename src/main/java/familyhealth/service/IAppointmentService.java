package familyhealth.service;

import familyhealth.model.Appointment;
import familyhealth.model.dto.AppointmentDTO;

public interface IAppointmentService {
    Appointment getAppointment(Long id);
    Appointment createAppointment(AppointmentDTO appointmentDTO);
    Appointment updateAppointment(Long id, AppointmentDTO appointmentDTO);
    void deleteAppointment(Long id);
}
