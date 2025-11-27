package familyhealth.mapper;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MedicalResultResponse {

    private Long id;
    private String name;
    private String diagnose;
    private String note;
    private Float totalMoney;
    private LocalDateTime createdAt;
    private Appointment appointment;

    @Getter
    @Setter
    public static class Appointment {
        private Long id;
        private LocalDateTime time;
        private String status;
        private String note;
        private String location;
        private Doctor doctor;
        private Member member;
    }

    @Getter
    @Setter
    public static class Doctor {
        private Long id;
        private String fullname;
        private String expertise;
    }

    @Getter
    @Setter
    public static class Member {
        private Long id;
        private String fullname;
        private String gender;
        private String relation;
    }
}