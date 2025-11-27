package familyhealth.service;

import familyhealth.model.Appointment;
import familyhealth.model.Member;
import familyhealth.model.dto.AppointmentDTO;
import familyhealth.model.dto.request.ChangeStatusRequest;
import familyhealth.model.dto.response.AppointmentResponse;
import familyhealth.model.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAppointmentService {
    PageResponse<List<AppointmentResponse>> getAllAppointmentService(String[] search, boolean complete, Pageable pageable);

    Appointment getAppointment(Long id);
    Appointment createAppointment(AppointmentDTO appointmentDTO);
    Appointment updateAppointment(Long id, AppointmentDTO appointmentDTO);
    void deleteAppointment(Long id);

    PageResponse<List<AppointmentResponse>> getAppointmentsByDoctor(String status , String[] search, Pageable pageable);

    void changeStatus(ChangeStatusRequest request);

    AppointmentResponse getAppointmentDetails(Long id);
}
