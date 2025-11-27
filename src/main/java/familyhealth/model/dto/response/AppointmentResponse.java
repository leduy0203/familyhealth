package familyhealth.model.dto.response;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResponse {

    private Long id;
    private String time;
    private String status;
    private String note;
    private String location;
    private Doctor doctor;
    private Member member;
    private MedicalResult medicalResult;


    @Getter
    @Setter
    public static class Doctor {
        private Long id;
        private String fullName;
        private String expertise;
    }

    @Getter
    @Setter
    public static class Member {
        private Long id;
        private String fullName;
        private String relation;
        private String bhyt;
    }

    @Getter
    @Setter
    public static class MedicalResult {
        private Long id;
        private String name;
        private String note;
        private String diagnose;
        private Float totalMoney;
    }
}
