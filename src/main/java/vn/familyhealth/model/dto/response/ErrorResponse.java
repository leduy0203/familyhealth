package vn.familyhealth.model.dto.response;

import lombok.Builder;
import lombok.Setter;

import java.util.Date;

@Setter
@Builder
public class ErrorResponse {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
