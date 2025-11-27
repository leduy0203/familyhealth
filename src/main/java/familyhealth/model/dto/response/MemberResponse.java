package familyhealth.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MemberResponse {

    private Long id;
    private String fullname;
    private String idCard;
    private String gender;
    private LocalDateTime dateOfBirth;
    private String email;
    private String bhyt;

    private List<MedicalResultDTO> medicalResults;

    @Getter
    @Setter
    public static class MedicalResultDTO {
        private Long id;
        private String name;
        private String diagnose;
        private String note;
        private Float totalMoney;
        private LocalDateTime createdAt;
        private LocalDateTime appointmentTime;
        private String doctorName;
    }
}
