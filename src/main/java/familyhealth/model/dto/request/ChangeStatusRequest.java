package familyhealth.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeStatusRequest {

    private long appointmentId;
    private String status; // CONFIRMED, COMPLETED, CANCELLED
}
