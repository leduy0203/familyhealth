package familyhealth.mapper;

import familyhealth.model.Appointment;
import familyhealth.model.Doctor;
import familyhealth.model.Member;
import familyhealth.model.dto.AppointmentDTO;
import familyhealth.model.dto.response.AppointmentResponse;

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

    public static AppointmentResponse convertToAppointmentResponse(Appointment appointment) {
        if (appointment == null) return null;


        AppointmentResponse.Doctor doctor = null;
        if (appointment.getDoctor() != null) {
            doctor = new AppointmentResponse.Doctor();
            doctor.setId(appointment.getDoctor().getId());
            doctor.setFullName(appointment.getDoctor().getFullname());
            doctor.setExpertise(appointment.getDoctor().getExpertise() != null
                    ? appointment.getDoctor().getExpertise().name() : null);
        }

        AppointmentResponse.Member member = null;
        if (appointment.getMember() != null) {
            member = new AppointmentResponse.Member();
            member.setId(appointment.getMember().getId());
            member.setFullName(appointment.getMember().getFullname());
            member.setRelation(appointment.getMember().getRelation() != null
                    ? appointment.getMember().getRelation().name() : null);
            member.setBhyt(appointment.getMember().getBhyt());
        }

        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setTime(appointment.getTime() != null ? appointment.getTime().toString() : null);
        response.setStatus(appointment.getStatus() != null ? appointment.getStatus().name() : null);
        response.setNote(appointment.getNote());
        response.setLocation(appointment.getLocation());
        response.setDoctor(doctor);
        response.setMember(member);

        return response;
    }
}
