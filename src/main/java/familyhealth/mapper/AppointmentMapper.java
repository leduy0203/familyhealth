package familyhealth.mapper;

import familyhealth.model.Appointment;
import familyhealth.model.Doctor;
import familyhealth.model.Member;
import familyhealth.model.dto.AppointmentDTO;

public class AppointmentMapper {
    public static Appointment convertToAppointment(AppointmentDTO dto, Doctor doctor, Member member) {
        if (dto == null) return null;

        return Appointment.builder()
                .time(dto.getTime())
                .location(dto.getLocation())
                .status(dto.getStatus())
                .note(dto.getNote())
                .doctor(doctor)
                .member(member)
                .build();
    }
}
